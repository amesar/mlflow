package com.databricks.mlflow.client.objects;

/** Convenience class for easier API search. */
public class ParameterSearch {
    private String key;
    private String comparator;
    private String value;

    public ParameterSearch(String key, String comparator, String value) {
        this.key = key;
        this.comparator = comparator;
        this.value = value;
    }
    public String getKey() { return key; }
    public String getComparator() { return comparator; }
    public String getValue() { return value; }

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
