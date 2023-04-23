package com.example.demo.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.demo.api.Service.implementations.UserAuthentificationServiceImpl;
import com.example.demo.api.Service.implementations.UserServiceImpl;
import com.example.demo.api.models.LoginResponse;
import com.example.demo.api.models.User;
import com.example.demo.exception.AuthException;

import javax.validation.Valid;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
public class UserResource {

    @Autowired
    private UserServiceImpl service;

    @GetMapping(path = "/hello")
    public String helloWorld(){
      return "Hello from App";
    }

    @GetMapping("/users")
    public List<User> retrieveAllUsers(){
      return service.findAll();
    }

    @GetMapping(path = "/user")
    public User retrieveOneUser(@RequestHeader String Authorization){
      Optional<String> validation = UserAuthentificationServiceImpl.verifyToken(Authorization);
      if(!validation.isPresent()) {
        throw new AuthException("user not found or token invalid");
      }
      return service.findByName(validation.get());
    }

    @GetMapping(path="/login")
    public ResponseEntity<LoginResponse> login(@RequestHeader String Authorization){
      LoginResponse logIn = service.checkAuthCredentials(Authorization);
      if (!logIn.isState()) {
        return new ResponseEntity<LoginResponse>(logIn, HttpStatus.UNAUTHORIZED);
      }
      return new ResponseEntity<LoginResponse>(logIn, HttpStatus.ACCEPTED);
    }

    @PostMapping(path="/users")
    public ResponseEntity<Object> setUser(@Valid @RequestBody User user){
      service.save(user);
      URI location =ServletUriComponentsBuilder
           .fromCurrentRequest()
           .path("/{name}")
           .buildAndExpand(user.getName()).toUri();
           
      return ResponseEntity.created(location).build();
    }

    @PatchMapping(path="/users/{userName}")
    public ResponseEntity<Object> setUser(@Valid @RequestBody User user, @PathVariable String userName){
      int userNumber = service.update(user, userName);
      if (userNumber == 0) {
        return ResponseEntity.badRequest().build();
      }
      return ResponseEntity.ok().build();
    }

    @DeleteMapping(path="users/{userName}")
    public ResponseEntity<Object> deleteUser(@PathVariable String userName){
      int userNumber = service.deleteByName(userName, "");
      if (userNumber == 0) {
        return ResponseEntity.badRequest().build();
      }
      return ResponseEntity.ok().build();
    }
}
