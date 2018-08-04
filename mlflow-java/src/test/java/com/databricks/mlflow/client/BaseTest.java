package com.databricks.mlflow.client;

import org.testng.annotations.*;

public class BaseTest {
    static String host = "localhost";
    static int port = 5001;
    static ApiClient client ;

    @BeforeSuite
    public static void beforeSuite() throws Exception {
        client = new ApiClient(host,port);
    }

    public String createExperimentName() {
        return "TestExp_"+System.currentTimeMillis();
    }
}
