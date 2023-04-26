package com.example.demo.api.Service;
import java.io.IOException;
import java.util.List;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.example.demo.api.models.FileObject;
import com.example.demo.api.models.User;

public interface FileStorageService {

    FileObject saveAsBlobFile(User user, String assetName, byte[] asset);

    List<S3ObjectSummary> listAll(String userPath);

    FileObject getByName(String assetName, String userName) throws IOException;

    boolean deleteByName(String assetName, String userName) throws IOException;

}
