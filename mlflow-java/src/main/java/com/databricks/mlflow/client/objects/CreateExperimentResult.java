package com.databricks.mlflow.client.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateExperimentResult {
    private String experimentId;
    @JsonProperty("experimentId")
    public String getExperimentId() { return experimentId; }
    public void setExperimentId(String experimentId) { this.experimentId = experimentId; }

    @Override
    public String toString() {
        return
             "experimentId="+experimentId
        ;
    }
}
