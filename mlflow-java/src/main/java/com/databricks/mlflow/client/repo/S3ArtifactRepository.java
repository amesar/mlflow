package com.databricks.mlflow.client.repo;

import java.util.*;
import java.io.File;
import java.nio.file.Paths;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class S3ArtifactRepository implements ArtifactRepository {
    private String bucketName;
    private String basePath;
    private AmazonS3 s3Client ;

    public S3ArtifactRepository(String s3Uri) {
        s3Uri = s3Uri.replace("s3://","");
        int idx = s3Uri.indexOf("/");
        this.bucketName = s3Uri.substring(0,idx);
        this.basePath = s3Uri.substring(idx+1);
        this.s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new ProfileCredentialsProvider())
                .build();
    }

    public void logArtifact(String localFile, String artifactPath) throws Exception {
        File file = new File(localFile);
        String key = Paths.get(makePath(artifactPath),file.getName()).toString();
        PutObjectRequest request = new PutObjectRequest(bucketName, key, file);
        s3Client.putObject(request);
    }

    public void logArtifacts(String localDir, String artifactPath) throws Exception {
        File dir = new File(localDir);
        for (File file : dir.listFiles()) {
            logArtifact(file.getAbsolutePath(),artifactPath);
        }
    }

    public List<String> listArtifacts(String path) throws Exception {
        ListObjectsV2Result result = s3Client.listObjectsV2(bucketName,makePath(path));
        List<S3ObjectSummary> objects = result.getObjectSummaries();
        List<String> artifacts = new ArrayList();
        for (S3ObjectSummary os: objects) {
            artifacts.add(os.getKey());
        }
        Collections.sort(artifacts);
        return artifacts;
    }

    public String downloadArtifacts(String artifactPath) throws Exception {
        return makePath(artifactPath);
    }

    private String makePath(String artifactPath) {
        return Paths.get(basePath,artifactPath).toString();
    }
}
