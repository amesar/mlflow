package com.databricks.mlflow.client.repo;

import java.util.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardCopyOption.*;

public class LocalArtifactRepository implements ArtifactRepository {
    private String basePath;

    public LocalArtifactRepository(String basePath) {
        this.basePath = basePath;
    }

    public void logArtifact(String localFile, String artifactPath) throws Exception {
        Files.createDirectories(makePath(artifactPath));
        Path src = Paths.get(localFile);
        Path dst = makePath(artifactPath, src.getFileName().toString());
        Files.copy(src, dst, REPLACE_EXISTING);
    }

    public void logArtifacts(String localDir, String artifactPath) throws Exception {
        File dir = new File(localDir);
        for (File file : dir.listFiles()) {
            logArtifact(file.getAbsolutePath(),artifactPath);
        }
    }

    public List<String> listArtifacts(String path) throws Exception {
        List<String> artifacts = Arrays.asList(new File(basePath,path).list());
        Collections.sort(artifacts);
        return artifacts;
    }

    public String downloadArtifacts(String artifactPath) throws Exception {
        return new File(basePath, artifactPath).getAbsolutePath();
    }

    Path makePath(String artifactPath, String localFile) {
        return Paths.get(basePath,artifactPath,localFile);
    }

    Path makePath(String artifactPath) {
        return Paths.get(basePath,artifactPath);
    }
}
