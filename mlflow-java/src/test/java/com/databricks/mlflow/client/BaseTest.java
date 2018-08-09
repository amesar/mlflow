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
        client = new ApiClient(apiUri);
        client.setVerbose(true);
    }
}
