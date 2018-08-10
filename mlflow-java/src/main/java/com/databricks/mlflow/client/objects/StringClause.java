package com.databricks.mlflow.client.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StringClause {
    public StringClause() {
    }
    public StringClause(String comparator, String value) { 
        this.comparator = comparator;
        this.value = value;
    }

    @JsonProperty("comparator")
    private String comparator;
    public String getComparator() { return comparator; } ; 
    public void setComparator() { this.comparator = comparator; } ; 

    @JsonProperty("value")
    private String value;
    public String getValue() { return value; } ; 
    public void setValue() { this.value = value; } ; 

    @Override
    public String toString() {
        return
               "[comparator=" + comparator
             + "value=" + value
             + "]"
        ;
    }
}
