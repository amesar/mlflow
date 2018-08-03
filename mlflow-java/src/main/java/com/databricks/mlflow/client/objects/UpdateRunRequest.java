package com.databricks.mlflow.client.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateRunRequest {
    public UpdateRunRequest() {
    }
    public UpdateRunRequest(String runUuid, String status, long endTime) {
        this.runUuid = runUuid;
        this.status = status;
        this.endTime = endTime;
    }

    private String runUuid; 
    @JsonProperty("run_uuid")
    public String getRunUuid() { return runUuid; }
    public void setRunUuid(String runUuid) { this.runUuid = runUuid; }
 
    private String status; 
    @JsonProperty("status")
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
 
    private long endTime; 
    @JsonProperty("end_time")
    public long getEndTime() { return endTime; }
    public void setEndTime(long endTime) { this.endTime = endTime; }

    @Override
    public String toString() {
        return
              "runUuid=" + runUuid 
            + " status=" + status 
            + " endTime=" + endTime 
        ;
    }
}
