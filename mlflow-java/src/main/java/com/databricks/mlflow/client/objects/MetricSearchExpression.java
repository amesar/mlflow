package com.databricks.mlflow.client.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MetricSearchExpression {
    public MetricSearchExpression() {
    }
    public MetricSearchExpression(FloatClause floatClause, String key) {
        this.floatClause = floatClause;
        this.key = key;
    }

    @JsonProperty("float")
    private FloatClause floatClause;
    public FloatClause getFloatClause() { return floatClause; }
    public void setFloatClause(FloatClause string) { this.floatClause = floatClause; }
    
    @JsonProperty("key")
    private String key;
    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }
    
    @Override
    public String toString() {
        return
             "key="+key
             + " floatClause="+floatClause
        ;
    }
}
