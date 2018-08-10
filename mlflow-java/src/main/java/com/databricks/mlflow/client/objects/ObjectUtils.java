package com.databricks.mlflow.client.objects;

import java.util.*;
import java.util.stream.Collectors;

public class ObjectUtils {
    public static ParameterSearchRequest makeParameterSearchRequest(int [] experimentIds, ParameterSearch[] clauses) {
        List<ParameterSearchExpressionWrapper> expressions = new ArrayList();
        for (ParameterSearch cl: clauses) {
            StringClause clause = new StringClause(cl.getComparator(),cl.getValue());
            expressions.add(new ParameterSearchExpressionWrapper(new ParameterSearchExpression(clause,cl.getKey())));
        }
        List<Integer> expIds = Arrays.stream(experimentIds).boxed().collect(Collectors.toList());
        return new ParameterSearchRequest(expIds,expressions);
    }

    public static MetricSearchRequest makeMetricSearchRequest(int [] experimentIds, MetricSearch[] clauses) {
        List<MetricSearchExpressionWrapper> expressions = new ArrayList();
        for (MetricSearch cl: clauses) {
            FloatClause clause = new FloatClause(cl.getComparator(),cl.getValue());
            expressions.add(new MetricSearchExpressionWrapper(new MetricSearchExpression(clause,cl.getKey())));
        }
        List<Integer> expIds = Arrays.stream(experimentIds).boxed().collect(Collectors.toList());
        return new MetricSearchRequest(expIds,expressions);
    }
}
