package com.databricks.mlflow.client.objects;

import java.util.*;
import java.util.stream.Collectors;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class ObjectUtils {
    public static SearchRequest makeSearchRequest(int [] experimentIds, BaseSearch[] clauses) {
        List<SearchExpressionWrapper> expressions = new ArrayList<>();
        for (BaseSearch cl: clauses) {
            if (cl instanceof ParameterSearch) {
                StringClause clause = new StringClause(cl.getComparator(),((ParameterSearch)cl).getValue());
                expressions.add(new SearchExpressionWrapper(new ParameterSearchExpression(clause,cl.getKey())));
            } else {
                FloatClause clause = new FloatClause(cl.getComparator(),((MetricSearch)cl).getValue());
                expressions.add(new SearchExpressionWrapper(new MetricSearchExpression(clause,cl.getKey())));
            }
        }
        List<Integer> expIds = Arrays.stream(experimentIds).boxed().collect(Collectors.toList());
        return new SearchRequest(expIds,expressions);
    }

    private static ObjectMapper mapper = new ObjectMapper();
    private static ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();

    public static String toJson(Object obj) throws Exception {
        return writer.writeValueAsString(obj);
    }
}
