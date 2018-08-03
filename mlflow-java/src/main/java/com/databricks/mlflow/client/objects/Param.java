package com.databricks.mlflow.client.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Param {
    public Param() {
    }
    public Param(String key, String value) {
        this.key = key;
        this.value = value;
    }

    private String key; 
    @JsonProperty("key")
    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }
 
    private String value; 
    @JsonProperty("value")
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    @Override
    public String toString() {
        return
              "key=" + key 
            + " value=" + value 
        ;
    }
}
