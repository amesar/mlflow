package com.databricks.mlflow.client.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LogParam {
    public LogParam() {
    }
    public LogParam(String runUuid, String key, String value) {
        this.runUuid = runUuid;
        this.key = key;
        this.value = value;
    }
    private String runUuid; 
    @JsonProperty("run_uuid")
    public String getRunUuid() { return runUuid; }
    public void setRunUuid(String runUuid) { this.runUuid = runUuid; }

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
              "[runUuid=" + runUuid
            + " key=" + key
            + " value=" + value 
            + "]"
        ;
    }
}
