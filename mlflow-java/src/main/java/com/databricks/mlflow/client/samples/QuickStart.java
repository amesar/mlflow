package com.databricks.mlflow.client.samples;

import java.util.*;
import com.databricks.mlflow.client.ApiClient;
import com.databricks.api.proto.mlflow.Service.*;
import com.databricks.mlflow.client.objects.ObjectUtils;

public class QuickStart {
    public static void main(String [] args) throws Exception {
        (new QuickStart()).process(args);
    }

    void process(String [] args) throws Exception {
        if (args.length < 1) {
            System.out.println("ERROR: Missing MLflow Tracking URI");
            System.exit(1);
        }
        ApiClient client = new ApiClient(args[0]);

        String expName = "Exp_"+System.currentTimeMillis();
        long expId = client.createExperiment(expName);
        System.out.println("expName="+expName);
        System.out.println("expId="+expId);

        String user = System.getenv("USER");
        long startTime = System.currentTimeMillis();
        String sourceName = this.getClass().getSimpleName()+".java";

        CreateRun request = ObjectUtils.makeCreateRun(expId, "run_for_"+expId, SourceType.LOCAL, sourceName, startTime, user);
        RunInfo runInfo = client.createRun(request);
        System.out.println("\nRunInfo:\n"+runInfo);
        String runId = runInfo.getRunUuid();

        client.logParameter(runId, "min_samples_leaf", "2");
        client.logParameter(runId, "max_depth", "3");

        client.logMetric(runId, "auc", 2.12F);
        client.logMetric(runId, "accuracy_score", 3.12F);

        client.updateRun(runId, RunStatus.FINISHED, startTime+1001);
    }
}
