package com.databricks.mlflow.client;

import java.util.*;
import org.testng.Assert;
import com.databricks.mlflow.client.objects.*;

public class TestUtils {
    static void assertRunInfo(RunInfo runInfo, String experimentId, String user, String sourceName) {
        Assert.assertEquals(runInfo.getExperimentId(),experimentId);
        Assert.assertEquals(runInfo.getUserId(),user);
        Assert.assertEquals(runInfo.getSourceName(),sourceName);
    }

    static void assertParam(List<Param> params, String key, String value) {
        Assert.assertTrue(params.stream().filter(e -> e.getKey().equals(key) && e.getValue().equals(value)).findFirst().isPresent());
    }

    static void assertMetric(List<Metric> metrics, String key, Double value) {
        Assert.assertTrue(metrics.stream().filter(e -> e.getKey().equals(key) && Double.compare(e.getValue(),value)==0).findFirst().isPresent());
    }

    static java.util.Optional<Experiment> getExperimentByName(List<Experiment> exps, String expName) {
        return exps.stream().filter(e -> e.getName().equals(expName)).findFirst();
    }

    static public String createExperimentName() {
        return "TestExp_"+System.currentTimeMillis();
    }
}
