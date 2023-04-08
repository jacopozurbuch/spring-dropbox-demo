package com.example.demo.api.Service;
import java.util.List;

import com.example.demo.api.models.FileObject;
import com.example.demo.api.models.User;

public interface FileStorageService {

    FileObject saveAsBlobFile(User user, String assetName, byte[] asset);

    List<FileObject> findAll(User user);

    //Asset findById(User user, UUID id);

    //Asset deleteById(User user, int id);

    FileObject findByName(String assetName);

    //Asset deleteByName(User user, String assetName);

}
