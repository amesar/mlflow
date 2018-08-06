package com.databricks.mlflow.client.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetMetricResponse {
    public GetMetricResponse() {
    }
    public GetMetricResponse(Metric metric) { 
        this.metric = metric;
    }

    @JsonProperty("metric")
    private Metric metric;
    public Metric getMetric() { return metric; } ; 
    public void setMetric() { this.metric = metric; } ; 

    @Override
    public String toString() {
        return
               "metric=" + metric
        ;
    }
}
