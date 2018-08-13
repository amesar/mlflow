package com.databricks.mlflow.client.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchExpressionWrapper {
    public SearchExpressionWrapper() {
    }
    public SearchExpressionWrapper(ParameterSearchExpression parameter) {
        this.parameter = parameter;
    }
    public SearchExpressionWrapper(MetricSearchExpression metric) {
        this.metric = metric;
    }

    @JsonProperty("parameter")
    private ParameterSearchExpression parameter;
    public ParameterSearchExpression getParameter() { return parameter; }
    public void setParameter(ParameterSearchExpression parameter) { this.parameter = parameter; }

    @JsonProperty("metric")
    private MetricSearchExpression metric;
    public MetricSearchExpression getMetric() { return metric; }
    public void setMetric(MetricSearchExpression metric) { this.metric = metric; }
    
    @Override
    public String toString() {
        return
             "[parameter="+parameter
             + " metric="+metric
             + "]"
        ;
    }
}
