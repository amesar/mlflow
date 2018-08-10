package com.databricks.mlflow.client.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FloatClause {
    public FloatClause() {
    }
    public FloatClause(String comparator, Double value) { 
        this.comparator = comparator;
        this.value = value;
    }

    @JsonProperty("comparator")
    private String comparator;
    public String getComparator() { return comparator; } ; 
    public void setComparator() { this.comparator = comparator; } ; 

    @JsonProperty("value")
    private Double value;
    public Double getValue() { return value; } ; 
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
