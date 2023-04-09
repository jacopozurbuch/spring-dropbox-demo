package com.example.demo.api.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.example.demo.api.Service.implementations.FileStorageServiceImpl;
import com.example.demo.api.Service.implementations.UserAuthentificationServiceImpl;
import com.example.demo.api.Service.implementations.UserServiceImpl;
import com.example.demo.api.models.LoginResponse;
import com.example.demo.api.models.User;
import com.example.demo.exception.UserException;


@RestController
public class FileStorageResource {
  
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private FileStorageServiceImpl asset;

    @GetMapping(path = "/files")
    public List<String> retrieveOneUser(@RequestHeader("Authorization") String Authorization){

      LoginResponse mess = UserAuthentificationServiceImpl.isAuthorized(Authorization);
        
      if(mess.isState() == false) {
        throw new UserException(mess.getMessage());
      }

      User user = userService.findByName(UserAuthentificationServiceImpl.getUserName(Authorization));
      List<S3ObjectSummary> assets = asset.listAll(user.getUser());

      List<String> s3ObjectKeys = new ArrayList<>();
      for (S3ObjectSummary objectSummary : assets) { 
        s3ObjectKeys.add(objectSummary.getKey());
      }
      return s3ObjectKeys;
    }

    @PostMapping(path="/upload")
    public String upload(@RequestParam("asset") String assetName,@RequestHeader("Authorization") String Authorization, @RequestPart MultipartFile file){
        
        LoginResponse mess = UserAuthentificationServiceImpl.isAuthorized(Authorization);
        
        if(mess.isState() == false) {
          throw new UserException(mess.getMessage());
        }

        User user = userService.findByName(UserAuthentificationServiceImpl.getUserName(Authorization));

        if(asset.findByName(assetName) != null) {
          return "asset with name " + assetName + " already exists!";
        }

        try {
            this.asset.saveAsBlobFile(user, assetName, file.getBytes());
            this.asset.save(user.getUser(), assetName, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Successfull";
    }


}