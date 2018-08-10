package com.databricks.mlflow.client.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ParameterSearchExpressionWrapper {
    public ParameterSearchExpressionWrapper() {
    }
    public ParameterSearchExpressionWrapper(ParameterSearchExpression parameter) {
        this.parameter = parameter;
    }

    @JsonProperty("parameter")
    private ParameterSearchExpression parameter;
    public ParameterSearchExpression getParameter() { return parameter; }
    public void setParameter(ParameterSearchExpression parameter) { this.parameter = parameter; }
    
    @Override
    public String toString() {
        return
             "parameter="+parameter
        ;
    }
}
