package com.example.demo.api.models;
import java.util.UUID;

public class FileObject {

    private String assetName;
    private UUID id;
    private byte[] blob;

    public FileObject(String assetName, byte[] blob) {
      this.assetName = assetName;
      this.blob = blob;
      this.id = UUID.randomUUID();
    }

    public String getAssetName() {
        return assetName;
    }
    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }

    public byte[] getBlob() {
        return blob;
    }
    public void setBlob(byte[] blob) {
        this.blob = blob;
    }
    
}
