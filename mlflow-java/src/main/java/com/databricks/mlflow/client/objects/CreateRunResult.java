package com.databricks.mlflow.client.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateRunResult {
    private String runUuid; 
    @JsonProperty("run_uuid")
    public String getRunUuid() { return runUuid; }
    public void setRunUuid(String runUuid) { this.runUuid = runUuid; }
 
    private String experimentId; 
    @JsonProperty("experiment_id")
    public String getExperimentId() { return experimentId; }
    public void setExperimentId(String experimentId) { this.experimentId = experimentId; }
 
    private String name; 
    @JsonProperty("name")
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
 
    private String source_type; 
    @JsonProperty("source_type")
    public String getSource_type() { return source_type; }
    public void setSource_type(String source_type) { this.source_type = source_type; }
 
    private String source_name; 
    @JsonProperty("source_name")
    public String getSource_name() { return source_name; }
    public void setSource_name(String source_name) { this.source_name = source_name; }
 
    private String userId; 
    @JsonProperty("user_id")
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
 
    private String status; 
    @JsonProperty("status")
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
 
    private String startTime; 
    @JsonProperty("start_time")
    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
 
    private String artifactUri; 
    @JsonProperty("artifact_uri")
    public String getArtifactUri() { return artifactUri; }
    public void setArtifactUri(String artifactUri) { this.artifactUri = artifactUri; }

    @Override
    public String toString() {
        return
              "runUuid=" + runUuid 
            + " experimentId=" + experimentId 
            + " name=" + name 
            + " source_type=" + source_type 
            + " source_name=" + source_name 
            + " userId=" + userId 
            + " status=" + status 
            + " startTime=" + startTime 
            + " artifactUri=" + artifactUri 
        ;
    }
}
