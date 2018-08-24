package com.databricks.mlflow.client.repo;

import java.util.*;

public class ArtifactRepositoryFactory {

    public static ArtifactRepository create(String uri) throws Exception {
        if (uri.startsWith("s3:")) {
            throw new UnsupportedOperationException("S3 support not yet implemented");
        } else if (uri.contains(":")) {
            throw new UnsupportedOperationException("Unsupported protocol: "+uri);
        } else {
            return new LocalArtifactRepository(uri);
        }
    }
}
