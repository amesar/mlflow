package com.databricks.mlflow.client.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MetricSearchExpressionWrapper {
    public MetricSearchExpressionWrapper() {
    }
    public MetricSearchExpressionWrapper(MetricSearchExpression metric) {
        this.metric = metric;
    }

    @JsonProperty("metric")
    private MetricSearchExpression metric;
    public MetricSearchExpression getMetric() { return metric; }
    public void setMetric(MetricSearchExpression metric) { this.metric = metric; }
    
    @Override
    public String toString() {
        return
             "metric="+metric
        ;
    }
}
