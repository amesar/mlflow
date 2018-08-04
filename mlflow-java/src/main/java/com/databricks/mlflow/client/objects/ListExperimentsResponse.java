package com.databricks.mlflow.client.objects;

import java.util.*;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ListExperimentsResponse{
    private List<Experiment> experiments; 

    @JsonProperty("experiments")
    public List<Experiment> getExperiments() { return experiments; }
    public void setExperiment(List<Experiment> experiments) { this.experiments= experiments; }
 
    @Override
    public String toString() {
        return
              "experiments=" + experiments
        ;
    }
}
