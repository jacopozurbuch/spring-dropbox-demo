package com.example.demo.api.Service.implementations;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.example.demo.api.Service.FileStorageService;
import com.example.demo.api.models.FileObject;
import com.example.demo.api.models.User;


import java.util.List;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private static List<FileObject> assets = new ArrayList<>();

    private static final Logger LOG = LoggerFactory.getLogger(FileStorageServiceImpl.class);

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${s3.bucket.name}")
    private String s3BucketName;

    private File convertMultiPartFileToFile(final MultipartFile multipartFile) {
        final File file = new File(multipartFile.getOriginalFilename());
        try (final FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(multipartFile.getBytes());
        } catch (IOException e) {
            LOG.error("Error {} occurred while converting the multipart file", e.getLocalizedMessage());
        }
        return file;
    }


    @Async
    public void save(String userPath, String fileName, final MultipartFile multipartFile) {
        try {
            final File file = convertMultiPartFileToFile(multipartFile);
            final String fullFileName = userPath + "/" + fileName;
            LOG.info("Uploading file with name {}", fileName);
            final PutObjectRequest putObjectRequest = new PutObjectRequest(s3BucketName, fullFileName, file);
            amazonS3.putObject(putObjectRequest);
            Files.delete(file.toPath()); // Remove the file locally created in the project folder
        } catch (AmazonServiceException e) {
            LOG.error("Error {} occurred while uploading file", e.getLocalizedMessage());
        } catch (IOException ex) {
            LOG.error("Error {} occurred while deleting temporary file", ex.getLocalizedMessage());
        }
    }

    public List<S3ObjectSummary> listAll(String userPath){
        List<S3ObjectSummary> objectSummary = new ArrayList<>();
        try {
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
                                                        .withBucketName(s3BucketName)
                                                        .withPrefix(userPath)
                                                        .withMaxKeys(10);
                                                    
            objectSummary =  amazonS3.listObjects(listObjectsRequest).getObjectSummaries();
        } catch(AmazonServiceException e){
            LOG.error("Error {} occurred while uploading file", e.getLocalizedMessage());
        }
        return objectSummary;
    }

    public FileObject saveAsBlobFile(User user,String assetName, byte[] blob) {
        FileObject asset = new FileObject(assetName, blob);
        FileStorageServiceImpl.assets.add(asset);
        return asset;
    }

    public FileObject getByName(String assetName, String userName) throws IOException { 
        S3ObjectInputStream inputStream = amazonS3.getObject(s3BucketName, userName + "/" + assetName).getObjectContent();
        byte[] contentAsBlob = inputStream.readAllBytes();
        inputStream.close();
        return new FileObject(assetName, contentAsBlob);
    }

    public boolean deleteByName(String assetName, String userName) throws IOException {
        amazonS3.deleteObject(s3BucketName, userName + "/" + assetName);
        return true;
    }

}
