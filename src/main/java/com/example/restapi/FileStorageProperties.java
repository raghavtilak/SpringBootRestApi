package com.example.restapi;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("file")
public class FileStorageProperties {
    private String uploadDir;

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
}
