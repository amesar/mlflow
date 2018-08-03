package com.databricks.mlflow.client.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateRunResponseWrapper {
    @JsonProperty("run")
    private CreateRunResponseWrapper2 run; 
    public CreateRunResponseWrapper2 getRun() { return run; }
    public void setRun(CreateRunResponseWrapper2 run) { this.run = run; }

    @Override
    public String toString() {
        return
              "run=" + run 
        ;
    }
}
