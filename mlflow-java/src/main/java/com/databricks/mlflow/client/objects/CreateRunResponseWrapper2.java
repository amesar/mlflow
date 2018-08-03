package com.databricks.mlflow.client.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateRunResponseWrapper2 {
    private CreateRunResponse info; 
    @JsonProperty("info")
    public CreateRunResponse getInfo() { return info; }
    public void setInfo(CreateRunResponse info) { this.info = info; }

    @Override
    public String toString() {
        return
              "info=" + info 
        ;
    }
}
