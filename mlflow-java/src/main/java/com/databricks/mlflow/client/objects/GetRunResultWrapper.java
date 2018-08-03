package com.databricks.mlflow.client.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetRunResultWrapper {
    @JsonProperty("run")
    private GetRunResult run; 
    public GetRunResult getRun() { return run; }
    public void setRun(GetRunResult run) { this.run = run; }

    @Override
    public String toString() {
        return
              "run=" + run 
        ;
    }
}
