package com.databricks.mlflow.client;

import org.apache.log4j.Logger;
import com.databricks.api.proto.mlflow.Service.*;
import com.databricks.mlflow.client.objects.ObjectUtils;
import com.databricks.mlflow.client.repo.ArtifactRepository;
import com.databricks.mlflow.client.repo.ArtifactRepositoryFactory;

public class RunContext implements AutoCloseable {
    private static final Logger logger = Logger.getLogger(RunContext.class);
    private ApiClient apiClient;
    private ArtifactRepository repo;
    private long experimentId;
    private String runId;
    private RunInfo runInfo;

    public RunContext(ApiClient apiClient, long experimentId, String runName, SourceType sourceType, String sourceName, String userId) throws Exception {
        this.apiClient = apiClient;
        this.experimentId = experimentId;

        CreateRun request = ObjectUtils.makeCreateRun(experimentId, runName, sourceType, sourceName, System.currentTimeMillis(), userId);
        RunInfo runInfo = apiClient.createRun(request);

        try {
            this.repo = ArtifactRepositoryFactory.create(runInfo.getArtifactUri());
        } catch (UnsupportedOperationException e) {
            logger.warn(e.getMessage());
        }
        this.runId = runInfo.getRunUuid();
        logger.debug("runId="+runId);
    }

    public void logParameter(String key, String value) throws Exception {
        apiClient.logParameter(runId, key, value);
    }

    public void logMetric(String key, float value) throws Exception {
        apiClient.logMetric(runId, key, value);
    }

    public void logArtifact(String localFile, String artifactPath) throws Exception {
        if (repo == null) {
            throw new UnsupportedOperationException("Cannot logArtifact() because no repo has been created");
        }
        repo.logArtifact(localFile, artifactPath);
    }

    public void logModel(String key) throws Exception {
        throw new UnsupportedOperationException("logModel() coming soon");
    }

    @Override
    public void close() throws Exception {
        apiClient.updateRun(runId, RunStatus.FINISHED, System.currentTimeMillis());
    }

    public String getRunId() {
        return runId;
    }

    public RunInfo getRunInfo() {
        return runInfo;
    }
}
