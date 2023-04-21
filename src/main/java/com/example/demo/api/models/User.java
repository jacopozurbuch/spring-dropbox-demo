package com.example.demo.api.models;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import com.example.demo.api.utils.GenerateHash;


public class User {

    private Integer id;

    @Size(min = 5, message = "this is the actual name")
    private String name;

    @Size(min = 3, message = "this is the user name")
    private String user;

    @Size(min = 8)
    private String email;

    private Auth password;

    @Past
    private LocalDate birthday;

    private List<UUID> assets;

    public User() {
        
    }

    public User(String name, String user, LocalDate birthday, String email, String password) {
        this.name = name;
        this.user = user;
        this.birthday = birthday;
        this.email = email;
        this.setPassword(password);
    }

    public User(String name, String userName, LocalDate birthday, String email) {
        this.name = name;
        this.user = userName;
        this.birthday = birthday;
        this.email = email;
    }

    public User(Integer id, String name, String userName, LocalDate birthday, String email) {
        this.id = id;
        this.name = name;
        this.user = userName;
        this.birthday = birthday;
        this.email = email;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id=id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public LocalDate getBirthday() {
        return this.birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public void setAssets(UUID assetUuid) {
        this.assets.add(assetUuid);
    }

    public List<UUID> getAssets() {
        return assets;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Auth getPassword() {
        return password;
    }

    public void setPassword(String password) {
        byte[] salt = GenerateHash.generateSalt();
        byte[] hash = GenerateHash.generate(password, salt);
        this.password = new Auth(hash, salt);
    }

}