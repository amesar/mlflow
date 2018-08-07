package com.databricks.mlflow.client.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FileInfo {
    public FileInfo() {
    }
    public FileInfo(String path, Boolean isDir, String fileSize) { 
        this.path = path;
        this.isDir = isDir;
        this.fileSize = fileSize;
    }

    @JsonProperty("path")
    private String path;
    public String getPath() { return path; } ; 
    public void setPath() { this.path = path; } ; 

    @JsonProperty("is_dir")
    private Boolean isDir;
    public Boolean getIsDir() { return isDir; } ; 
    public void setIsDir() { this.isDir = isDir; } ; 

    @JsonProperty("file_size")
    private String fileSize;
    public String getFileSize() { return fileSize; } ; 
    public void setFileSize() { this.fileSize = fileSize; } ; 

    @Override
    public String toString() {
        return
               "[path=" + path
             + " isDir=" + isDir
             + " fileSize=" + fileSize+"]"
        ;
    }
}
