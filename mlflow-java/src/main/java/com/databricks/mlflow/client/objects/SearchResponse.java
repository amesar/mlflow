package com.databricks.mlflow.client.objects;

import java.util.*;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SearchResponse {
    private List<Run> runs = Collections.emptyList();
    public List<Run> getRuns() { return runs; }
    public void setRuns(List<Run> runs) { this.runs = runs; }

    @Override
    public String toString() {
        return
             "#runs="+runs.size()
        ;
    }
}