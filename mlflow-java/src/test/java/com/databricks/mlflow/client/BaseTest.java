package com.databricks.mlflow.client;

import java.util.List;
import org.testng.Assert;
import org.testng.annotations.*;
import com.databricks.mlflow.client.objects.*;

public class BaseTest {
    static String apiUriDefault = "http://localhost:5001_XX";
    static ApiClient client ;

    @BeforeSuite
    public static void beforeSuite() throws Exception {
        String apiUriProp = System.getenv("MLFLOW_TRACKING_URI");
        String apiUri = apiUriProp == null ? apiUriDefault : apiUriProp;
        client = new ApiClient(apiUri);
        client.setVerbose(true);
    }
}
