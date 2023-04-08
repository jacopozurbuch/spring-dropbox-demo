package com.example.demo.api.models;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import javax.validation.constraints.Past;
import javax.validation.constraints.Size;


public class User {

    private Integer id;

    @Size(min = 3, message = "this is the actual name")
    private String name;

    @Size(min = 3, message = "this is the actual surname")
    private String surname;

    @Size(min = 7, message = "this is the user name")
    private String user;

    @Past
    private LocalDate birthday;

    private List<UUID> assets;

    protected User() {
        
    }

    public User(String name, String surname, String userName, LocalDate birthday) {
        this.name = name;
        this.surname = surname;
        this.user = userName;
        this.birthday = birthday;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

}