package com.databricks.mlflow.client.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ParameterSearchExpression {
    public ParameterSearchExpression() {
    }
    public ParameterSearchExpression(StringClause stringClause, String key) {
        this.stringClause = stringClause;
        this.key = key;
    }

    @JsonProperty("string")
    private StringClause stringClause;
    public StringClause getStringClause() { return stringClause; }
    public void setStringClause(StringClause string) { this.stringClause = stringClause; }
    
    @JsonProperty("key")
    private String key;
    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }
    
    @Override
    public String toString() {
        return
             "key="+key
             + " stringClause="+stringClause
        ;
    }
}
