package com.example.demo.api.Service.implementations;

import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.example.demo.api.models.Auth;
import com.example.demo.api.models.LoginResponse;
import com.example.demo.api.models.User;
import com.example.demo.api.repository.UserRepository;
import com.example.demo.api.utils.GenerateHash;
import com.example.demo.exception.AuthException;

@Component
public class UserAuthentificationServiceImpl  {

    private static Algorithm algorithm;

    private static UserRepository userRepository;

    private static JWTVerifier verifier;

    private static String issuer;

    @Autowired
    public void setUserRepository(UserRepository repo) {
      UserAuthentificationServiceImpl.userRepository = repo;
    }
    
    @Value("${jwt.secret}")
    public void setAlgorithm(String secret){
      UserAuthentificationServiceImpl.algorithm = Algorithm.HMAC256(secret);
    }

    @Value("${jwt.issuer}")
    public void setIssuer(String issuer){
      UserAuthentificationServiceImpl.issuer = issuer;
    }

    @Value("${jwt.issuer}")
    public void setVerifier(String issuer){
      UserAuthentificationServiceImpl.verifier = JWT.require(algorithm)
                                                    .withIssuer(issuer)
                                                    .build();
    }
                                            
    public static LoginResponse isAuthorized(String encodedAuthString){
      String[] authString = decodeString(encodedAuthString);
      String userName = authString[0];
      String passwd = authString[1];
      Optional<Auth> auth = userRepository.getPassword(userName);
      if(auth.isPresent()) {
        Auth userAuth = auth.get();
        byte[] savedHash = userAuth.getHash();
        byte[] providedHash = GenerateHash.generate(passwd, userAuth.getSalt());
  
        if (GenerateHash.compareHash(savedHash, providedHash)) {
            return new LoginResponse("password is valid", true, createToken(userName, issuer));
        } else {
            return new LoginResponse("password is not valid", false);
        }
      }
      return new LoginResponse("user not found", false);    
    }

    public static String getUserName(String encodedAuthString){
      String[] authString = decodeString(encodedAuthString);
      return authString[0];
    }

    public static Optional<String> verifyToken(String encodedAuthString) {
      String[] authString = decodeString(encodedAuthString);
      String jwtToken = authString[0];
      try {
        DecodedJWT decodedJWT = verifier.verify(jwtToken);
        Claim claim = decodedJWT.getClaim("username");
        Optional<User> user = userRepository.findByName(claim.asString());
        if(user.isPresent()){
          return Optional.of(user.get().getUser());
          }
        } catch (JWTVerificationException e) {
          System.out.println(e.getMessage());
        }
      return Optional.ofNullable(null);
    }

    private static String[] decodeString(String authString){  
      // Header is in the format "Basic 5tyc0uiDat4"
      // We need to extract data before decoding it back to original string
      String[] authParts = authString.split("\\s+");
      String method = authParts[0];
      String authInfo = authParts[1];
      // Decode the data back to original string
      if(method.equals("Basic")) {
          byte[] bytes = Base64.getDecoder().decode(authInfo);
          return new String(bytes).split(":");
      } else if (method.equals("Bearer")) {
          String[] bearer = {authInfo};  
          return bearer;
      } else {
          throw new AuthException("wrong authentication");
      }
          
    }
 
    private static String createToken(String userName, String issuer) {
      return JWT.create()
                .withIssuer(issuer)
                .withSubject("Baeldung Details")
                .withClaim("username", userName)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600000L)) // last for 6 minutes
                .withJWTId(UUID.randomUUID().toString())
                .withNotBefore(new Date())
                .sign(algorithm);
    }

}
