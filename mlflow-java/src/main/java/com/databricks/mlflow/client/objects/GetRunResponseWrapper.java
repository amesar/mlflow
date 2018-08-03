package com.databricks.mlflow.client.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetRunResponseWrapper {
    @JsonProperty("run")
    private GetRunResponse run; 
    public GetRunResponse getRun() { return run; }
    public void setRun(GetRunResponse run) { this.run = run; }

    @Override
    public String toString() {
        return
              "run=" + run 
        ;
    }
}
