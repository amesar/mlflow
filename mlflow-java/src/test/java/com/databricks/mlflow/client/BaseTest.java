package com.databricks.mlflow.client;

import org.testng.annotations.*;

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
}
