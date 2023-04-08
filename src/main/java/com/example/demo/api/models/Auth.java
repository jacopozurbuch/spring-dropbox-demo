package com.example.demo.api.models;

public class Auth {
    
private byte[] salt;
private byte[] hash;

public Auth(byte[] hash, byte[]salt) {
    this.hash = hash;
    this.salt = salt;
}
   
public byte[] getHash() {
    return hash;
}
public void setHash(byte[] hash) {
    this.hash = hash;
}

public byte[] getSalt() {
    return salt;
}
public void setSalt(byte[] salt) {
    this.salt = salt;
}


}
