package com.databricks.mlflow.client.objects;

import java.util.*;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GetMetricHistoryResponse {
    public GetMetricHistoryResponse() {
    }
    public GetMetricHistoryResponse(List<Metric> metrics) { 
        this.metrics = metrics;
    }

    @JsonProperty("metrics")
    private List<Metric> metrics = Collections.emptyList();
    public List<Metric> getMetrics() { return metrics; } ; 
    public void setMetrics() { this.metrics = metrics; } ; 

    @Override
    public String toString() {
        return
               "metrics=" + metrics
        ;
    }
}
