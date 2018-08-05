package com.databricks.mlflow.client.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateExperimentRequest {
    public CreateExperimentRequest() {
    }
    public CreateExperimentRequest(String name) {
        this.name = name;
    }
    @JsonProperty("name")
    private String name;
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Override
    public String toString() {
        return
             "name="+name
        ;
    }
}
