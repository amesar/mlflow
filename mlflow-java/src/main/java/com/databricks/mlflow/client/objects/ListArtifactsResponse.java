package com.databricks.mlflow.client.objects;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ListArtifactsResponse {
    public ListArtifactsResponse() {
    }
    public ListArtifactsResponse(List<FileInfo> files, String rootUri) { 
        this.files = files;
        this.rootUri = rootUri;
    }

    @JsonProperty("files")
    private List<FileInfo> files;
    public List<FileInfo> getFiles() { return files; } ; 
    public void setFiles() { this.files = files; } ; 

    @JsonProperty("root_uri")
    private String rootUri;
    public String getRootUri() { return rootUri; } ; 
    public void setRootUri() { this.rootUri = rootUri; } ; 

    @Override
    public String toString() {
        return
               "files=" + files
             + "rootUri=" + rootUri
        ;
    }
}
