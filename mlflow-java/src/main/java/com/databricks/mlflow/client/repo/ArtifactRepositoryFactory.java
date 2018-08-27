package com.databricks.mlflow.client.repo;

import java.util.*;

public class ArtifactRepositoryFactory {

    public static ArtifactRepository create(String uri) throws Exception {
        if (uri.startsWith("s3:")) {
            return new S3ArtifactRepository(uri);
        } else if (uri.startsWith("dbfs:")) {
            throw new UnsupportedOperationException("DBFS protocol coming shortly: "+uri);
        } else if (uri.contains(":")) {
            throw new UnsupportedOperationException("Unsupported protocol: "+uri);
        } else {
            return new LocalArtifactRepository(uri);
        }
    }
}
