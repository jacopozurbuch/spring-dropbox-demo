package com.example.demo.api.Service.implementations;

import java.util.Base64;
import java.util.Hashtable;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.api.models.Auth;
import com.example.demo.api.models.LoginResponse;
import com.example.demo.api.models.User;
import com.example.demo.api.utils.GenerateHash;

@Component
public class UserAuthentificationServiceImpl  {
    
    private static Map<String, Auth> userAuthList = new Hashtable<>();

    private static Algorithm algorithm;

    @Value("${jwt.secret}")
    public void setAlgorithmStatic(String secret){
      algorithm = Algorithm.HMAC256(secret);
    }

    public static LoginResponse isAuthorized(String encodedAuthString) {
      String[] authString = decodeString(encodedAuthString);
      String userName = authString[0];
      String passwd = authString[1];
      for ( String key: userAuthList.keySet()) {
        if ( userName.equals(key)) {
            Auth userAuth = userAuthList.get(userName);
            byte[] savedHash = userAuth.getHash();
            byte[] providedHash = GenerateHash.generate(passwd, userAuth.getSalt());

            if (GenerateHash.compareHash(savedHash, providedHash)) {
                return new LoginResponse("password is valid", true);
            } else {
                return new LoginResponse("password is not valid", false);
            }
        } 
      }
      return new LoginResponse("user not found", false);    
    }

    public static String getUserName(String encodedAuthString) {
      String[] authString = decodeString(encodedAuthString);
      return authString[0];
    }

    public static void saveUserPasswd(User user, String passwd) {
      byte[] salt = GenerateHash.generateSalt();  
      byte[] hash = GenerateHash.generate(passwd, salt);
      userAuthList.put(user.getUser(), new Auth(hash,salt));
    }

    private static String[] decodeString(String authString){
         
        // Header is in the format "Basic 5tyc0uiDat4"
        // We need to extract data before decoding it back to original string
        String[] authParts = authString.split("\\s+");
        String authInfo = authParts[1];
        // Decode the data back to original string
        byte[] bytes = null;
        bytes = Base64.getDecoder().decode(authInfo);

        return new String(bytes).split(":");
        
    }

}
