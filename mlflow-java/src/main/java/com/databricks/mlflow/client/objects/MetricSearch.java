package com.databricks.mlflow.client.objects;

/** Convenience class for easier API search. */
public class MetricSearch {
    private String key;
    private String comparator;
    private Double value;

    public MetricSearch(String key, String comparator, Double value) {
        this.key = key;
        this.comparator = comparator;
        this.value = value;
    }
    public String getKey() { return key; }
    public String getComparator() { return comparator; }
    public Double getValue() { return value; }

    @Override
    public String toString() {
        return
             "[key="+key
             + " comparator="+comparator
             + " value="+value
             + "]"
        ;
    }
}
