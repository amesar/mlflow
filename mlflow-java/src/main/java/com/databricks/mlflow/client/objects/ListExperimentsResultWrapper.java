package com.databricks.mlflow.client.objects;

import java.util.*;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ListExperimentsResultWrapper {
    private List<ExperimentSummary> experiments; 

    @JsonProperty("experiments")
    public List<ExperimentSummary> getExperiments() { return experiments; }
    public void setExperiment(List<ExperimentSummary> experiments) { this.experiments= experiments; }
 
    @Override
    public String toString() {
        return
              "experiments=" + experiments
        ;
    }
}
