package com.databricks.mlflow.client;

import org.testng.annotations.*;

public class HttpTest extends BaseTest {
    //@Test (expectedExceptions = HttpClientException.class) // TODO: server should throw 4xx
    @Test (expectedExceptions = HttpServerException.class) 
    public void getExperiment() throws Exception {
        client.get("experiments/get?experiment_id=NON_EXISTENT_ID");
    }

    @Test (expectedExceptions = HttpClientException.class) 
    public void nonExistentPath() throws Exception {
        client.get("BAD_PATH");
    }
}
