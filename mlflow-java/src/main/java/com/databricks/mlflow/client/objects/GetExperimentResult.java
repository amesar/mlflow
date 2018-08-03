package com.databricks.mlflow.client.objects;

import java.util.*;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GetExperimentResult {
    @JsonProperty("experiment")
    private ExperimentSummary experiment; 
    public ExperimentSummary getExperiment() { return experiment; }
    public void setExperiment(ExperimentSummary experiment) { this.experiment= experiment; }

    private List<RunInfo> runs;
    @JsonProperty("runs")
    public List<RunInfo> getRuns() { return runs; }
    public void setRuns(List<RunInfo> runs) { this.runs = runs; }
 
    @Override
    public String toString() {
        return
              "experiment=" + experiment
        ;
    }
}
