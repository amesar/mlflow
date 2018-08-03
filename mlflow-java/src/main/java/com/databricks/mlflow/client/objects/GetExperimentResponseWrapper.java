package com.databricks.mlflow.client.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetExperimentResponseWrapper {
    private ExperimentSummary experiment; 
    @JsonProperty("experiment")
    public ExperimentSummary getExperiment() { return experiment; }
    public void setExperiment(ExperimentSummary experiment) { this.experiment= experiment; }
 
    @Override
    public String toString() {
        return
              "experiment=" + experiment
        ;
    }
}
