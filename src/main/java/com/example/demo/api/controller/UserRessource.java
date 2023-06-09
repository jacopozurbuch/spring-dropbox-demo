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

import com.example.demo.api.Service.implementations.UserServiceImpl;
import com.example.demo.api.models.LoginResponse;
import com.example.demo.api.models.User;
import com.example.demo.exception.UserNotFoundException;

import javax.validation.Valid;

import java.net.URI;
import java.util.List;

@RestController
public class UserRessource {

    @Autowired
    private UserServiceImpl service;

    @GetMapping(path = "/hello")
    public String helloWorld(){
      return "Hello from Demo-Dropbox";
    }

    @GetMapping("/users")
    public List<User> retrieveAllUsers(){
      return service.findAll();
    }

    @GetMapping(path = "/user")
    public ResponseEntity<User> retrieveOneUser(@RequestHeader String Authorization){
      try {
        return new ResponseEntity<User>(service.findByName(Authorization), HttpStatus.OK);
      } catch(UserNotFoundException e) {
        return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
      }
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
