package com.databricks.mlflow.client.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

// Autogenerated on 2018-08-03 03:05:37 UTC

public class Experiment {
    @JsonProperty("experiment_id")
    private String experimentId; 
    public String getExperimentId() { return experimentId; }
    public void setExperimentId(String experimentId) { this.experimentId = experimentId; }
 
    @JsonProperty("name")
    private String name; 
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
 
    @JsonProperty("artifact_location")
    private String artifactLocation; 
    public String getArtifactLocation() { return artifactLocation; }
    public void setArtifactLocation(String artifactLocation) { this.artifactLocation = artifactLocation; }

    @Override
    public String toString() {
        return
              "experimentId=" + experimentId 
            + " name=" + name 
            + " artifactLocation=" + artifactLocation 
        ;
    }
}