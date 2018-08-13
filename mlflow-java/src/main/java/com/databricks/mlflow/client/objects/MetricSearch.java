package com.databricks.mlflow.client.objects;

/** Convenience class for easier API search. */
public class MetricSearch extends BaseSearch {
    private Double value;

    public MetricSearch(String key, String comparator, Double value) {
        super(key, comparator);
        this.value = value;
    }
    public Double getValue() { return value; }

    @Override
    public String toString() {
        return
             "[key="+getKey()
             + " comparator="+getComparator()
             + " value="+value
             + "]"
        ;
    }
}
