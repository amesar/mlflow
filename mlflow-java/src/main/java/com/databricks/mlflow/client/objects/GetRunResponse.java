package com.databricks.mlflow.client.objects;

import java.util.*;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GetRunResponse {
    @JsonProperty("info")
    private RunInfo info; 
    public RunInfo getInfo() { return info; }
    public void setInfo(RunInfo info) { this.info = info; }

    @JsonProperty("data")
    private RunData data; 
    public RunData getData() { return data; }
    public void setData(RunData data) { this.data = data; }

    @Override
    public String toString() {
        return
              "info=" + info 
              + " data=" + data 
        ;
    }
}
