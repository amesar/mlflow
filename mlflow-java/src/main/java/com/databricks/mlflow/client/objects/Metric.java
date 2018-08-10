package com.databricks.mlflow.client.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Metric {
    private String key; 
    @JsonProperty("key")
    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }
 
    private Double value; 
    @JsonProperty("value")
    public Double getValue() { return value; }
    public void setValue(Double value) { this.value = value; }
 
    private String timestamp; 
    @JsonProperty("timestamp")
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    @Override
    public String toString() {
        return
              "[key=" + key 
            + " value=" + value 
            + " timestamp=" + timestamp 
            + "]"
        ;
    }
}
