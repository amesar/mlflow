package com.databricks.mlflow.client;

import java.util.List;
import org.testng.Assert;
import org.testng.annotations.*;
import com.databricks.mlflow.client.objects.*;

public class BaseTest {
    static String apiUri = "http://localhost:5001";
    static ApiClient client ;

    @BeforeSuite
    public static void beforeSuite() throws Exception {
        client = new ApiClient(apiUri, true);
    }

    public String createExperimentName() {
        return "TestExp_"+System.currentTimeMillis();
    }

    void assertRunInfo(RunInfo runInfo, String experimentId, String user, String sourceName) {
        Assert.assertEquals(runInfo.getExperimentId(),experimentId);
        Assert.assertEquals(runInfo.getUserId(),user);
        Assert.assertEquals(runInfo.getSourceName(),sourceName);
    }
    void assertParam(List<Param> params, String key, String value) {
        Assert.assertTrue(params.stream().filter(e -> e.getKey().equals(key) && e.getValue().equals(value)).findFirst().isPresent());
    }
    void assertMetric(List<Metric> metrics, String key, double value) {
        Assert.assertTrue(metrics.stream().filter(e -> e.getKey().equals(key) && e.getValue().equals(value)).findFirst().isPresent());
    }
    java.util.Optional<Experiment> getExperimentByName(List<Experiment> exps, String expName) {
        return exps.stream().filter(e -> e.getName().equals(expName)).findFirst();
    }
}
