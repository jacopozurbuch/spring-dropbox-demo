package com.example.demo.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.demo.api.Service.implementations.UserServiceImpl;
import com.example.demo.api.models.LoginResponse;
import com.example.demo.api.models.User;
import com.example.demo.api.models.UserInit;
import com.example.demo.exception.UserNotFoundException;

import javax.validation.Valid;

import java.net.URI;
import java.util.List;

@RestController
public class UserResource {

    @Autowired
    private UserServiceImpl service;

    @GetMapping(path = "/hello")
    public String helloWorld(){
      return "Hello World";
    }

    @GetMapping("/users")
    public List<User> retrieveAllUsers(){
      return service.findAll();
    }

    @GetMapping(path = "/users/{id}")
    public User retrieveOneUser(@PathVariable int id){

      User user = service.findById(id);
      if(user == null) {
        throw new UserNotFoundException("id=" + id);
      }

      return user;
    }

    @GetMapping(path="/login")
    public String login(@RequestHeader String Authorization){
      LoginResponse logIn = service.checkAuthCredentials(Authorization);
      return logIn.getMessage();
    }

    @PostMapping(path="/users")
    public ResponseEntity<Object> setUser(@Valid @RequestBody UserInit user){
      User savedUser = service.save(user.getUser(), user.getPasswd());
      URI location =ServletUriComponentsBuilder
           .fromCurrentRequest()
           .path("/{id}")
           .buildAndExpand(savedUser.getId()).toUri();
           
      return ResponseEntity.created(location).build();

    }

    @DeleteMapping(path="users/{userName}")
    public String deleteUser(@PathVariable String userName, @RequestHeader String authorization){
      String message = service.deleteByName(userName, authorization);
      if(message == null) {
        throw new UserNotFoundException("name=" + userName);
      }
      return message;
    }
}
