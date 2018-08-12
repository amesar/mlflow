package com.databricks.mlflow.client.objects;

import java.util.*;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RunData {
    @JsonProperty("params")
    private List<Param> params = Collections.emptyList();
    public List<Param> getParams() { return params; }
    public void setParams(List<Param> params) { this.params = params; }

    @JsonProperty("metrics")
    private List<Metric> metrics = Collections.emptyList();
    public List<Metric> getMetrics() { return metrics; }
    public void setMetrics(List<Metric> metrics) { this.metrics = metrics; }

    @Override
    public String toString() {
        return
             "[#params="+params.size()
             + " #metrics="+params.size()
             + "]"
        ;
    }
}