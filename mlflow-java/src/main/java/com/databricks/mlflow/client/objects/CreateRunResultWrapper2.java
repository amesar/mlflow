package com.databricks.mlflow.client.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateRunResultWrapper2 {
    private CreateRunResult info; 
    @JsonProperty("info")
    public CreateRunResult getInfo() { return info; }
    public void setInfo(CreateRunResult info) { this.info = info; }

    @Override
    public String toString() {
        return
              "info=" + info 
        ;
    }
}
