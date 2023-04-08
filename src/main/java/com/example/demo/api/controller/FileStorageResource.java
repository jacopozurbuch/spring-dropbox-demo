package com.example.demo.api.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.api.Service.implementations.FileStorageServiceImpl;
import com.example.demo.api.Service.implementations.UserServiceImpl;
import com.example.demo.api.models.FileObject;
import com.example.demo.api.models.User;
import com.example.demo.exception.UserNotFoundException;


@RestController
public class FileStorageResource {
  
    @Autowired
    private UserServiceImpl service;

    @Autowired
    private FileStorageServiceImpl asset;

    @GetMapping(path = "/files")
    public List<FileObject> retrieveOneUser(@RequestParam("user") String userName){

      User user = service.findByName(userName);
      if(user == null) {
        throw new UserNotFoundException("user =" + userName);
      }
      List<FileObject> assets = asset.findAll(user);

      return assets;
    }

    @PostMapping(path="/upload")
    public String upload(@RequestParam("user") String userName,@RequestParam("asset") String assetName, @RequestPart MultipartFile file){
        User user = service.findByName(userName);
        
        if(user == null) {
          return "user not found"; //throw new UserNotFoundException("user =" + user.getUser());
        }
        if(asset.findByName(assetName) != null) {
          return "asset with name " + assetName + " already exists!";
        }

        try {
            this.asset.saveAsBlobFile(user, assetName, file.getBytes());
            this.asset.save(file);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "Successfull";
    }


}