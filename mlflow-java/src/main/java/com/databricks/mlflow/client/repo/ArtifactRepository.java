package com.databricks.mlflow.client.repo;

import java.util.List;

public interface ArtifactRepository {

    void logArtifact(String localFile, String artifactPath) throws Exception ;

    void logArtifacts(String localFile, String artifactPath) throws Exception ;

    // TODO: should be FileInfo
    List<String> listArtifacts(String path) throws Exception ;

    String downloadArtifacts(String artifactPath) throws Exception ;
}
