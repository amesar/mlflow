package com.databricks.mlflow.client.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateExperimentRequest {
    private String name;
    @JsonProperty("name")
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Override
    public String toString() {
        return
             "name="+name
        ;
    }
}
