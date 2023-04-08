package com.example.demo.api.utils;
import java.security.SecureRandom;
import java.util.Arrays;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class GenerateHash {

   static public byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }
    
   static public byte[] generate(String passwd, byte[] salt) {

    MessageDigest md;
    try {
        md = MessageDigest.getInstance("SHA-512");
        md.update(salt);
        return  md.digest(passwd.getBytes(StandardCharsets.UTF_8));
    } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    return null;
   }

   static public boolean compareHash(byte[] savedHash, byte[] providedHash) {
     return Arrays.equals(savedHash, providedHash);
   }
    


}
