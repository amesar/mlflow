package com.databricks.mlflow.client.objects;

import java.util.*;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RunInfo {
    @JsonProperty("run_uuid")
    private String runUuid; 
    public String getRunUuid() { return runUuid; }
    public void setRunUuid(String runUuid) { this.runUuid = runUuid; }
 
    @JsonProperty("experiment_id")
    private String experimentId; 
    public String getExperimentId() { return experimentId; }
    public void setExperimentId(String experimentId) { this.experimentId = experimentId; }
 
    @JsonProperty("name")
    private String name; 
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
 
    @JsonProperty("source_type")
    private String sourceType; 
    public String getSourceType() { return sourceType; }
    public void setSourceType(String sourceType) { this.sourceType = sourceType; }
 
    @JsonProperty("source_name")
    private String sourceName; 
    public String getSourceName() { return sourceName; }
    public void setSourceName(String sourceName) { this.sourceName = sourceName; }
 
    @JsonProperty("user_id")
    private String userId; 
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
 
    @JsonProperty("status")
    private String status; 
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
 
    @JsonProperty("start_time")
    private String startTime; 
    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    @JsonProperty("end_time")
    private String endTime; 
    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
 
    @JsonProperty("artifact_uri")
    private String artifactUri; 
    public String getArtifactUri() { return artifactUri; }
    public void setArtifactUri(String artifactUri) { this.artifactUri = artifactUri; }

    @Override
    public String toString() {
        return
              "runUuid=" + runUuid 
            + " experimentId=" + experimentId 
            + " name=" + name 
            + " sourceType=" + sourceType 
            + " source_name=" + sourceName 
            + " userId=" + userId 
            + " status=" + status 
            + " startTime=" + startTime 
            + " endTime=" + endTime 
            + " artifactUri=" + artifactUri 
        ;
    }
}
