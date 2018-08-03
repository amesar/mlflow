package com.databricks.mlflow.client.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateRunResultWrapper {
    @JsonProperty("run")
    private CreateRunResultWrapper2 run; 
    public CreateRunResultWrapper2 getRun() { return run; }
    public void setRun(CreateRunResultWrapper2 run) { this.run = run; }

    @Override
    public String toString() {
        return
              "run=" + run 
        ;
    }
}
