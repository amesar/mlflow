package com.databricks.mlflow.client.objects;

import java.util.*;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SearchRequest {
    public SearchRequest() {
    }
    public SearchRequest(List<Integer> experimentIds, List<SearchExpressionWrapper> andedExpressions) {
        this.experimentIds = experimentIds;
        this.andedExpressions = andedExpressions;
    }

    @JsonProperty("experiment_ids")
    private List<Integer> experimentIds = Collections.emptyList();
    public List<Integer> getExperimentIds() { return experimentIds; }
    public void setExperimentIds(List<Integer> experimentIds) { this.experimentIds = experimentIds; }

    @JsonProperty("anded_expressions")
    private List<SearchExpressionWrapper> andedExpressions = Collections.emptyList();
    public List<SearchExpressionWrapper> getAndedExpressions() { return andedExpressions; }
    public void setAndedExpressions(List<SearchExpressionWrapper> andedExpressions) { this.andedExpressions = andedExpressions; }

    @Override
    public String toString() {
        return
             "[#experimentIds="+experimentIds.size()
             + " #andedExpressions="+andedExpressions.size()
             + "]"
        ;
    }
}
