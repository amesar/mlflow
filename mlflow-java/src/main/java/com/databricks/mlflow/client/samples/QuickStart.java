package com.databricks.mlflow.client.samples;

import java.io.PrintWriter;
import com.databricks.mlflow.client.ApiClient;
import com.databricks.mlflow.client.RunContext;
import com.databricks.mlflow.client.objects.*;

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
        String expId = client.createExperiment(expName);
         System.out.println("expName="+expName);
        System.out.println("expId="+expId);

        String runId;
        String sourceName = this.getClass().getSimpleName()+".java";
        try (RunContext run = new RunContext(client, expId, "MyRun", "LOCAL", sourceName, System.getenv("USER")) ) {
            runId = run.getRunId();
            System.out.println("runId="+runId);
            run.logParameter("min_samples_leaf", "2");
            run.logMetric("auc", 2.12F);
            String localFile = "info.txt";
            try (PrintWriter w = new PrintWriter(localFile)) {
                w.write("Some information");
            };
            run.logArtifact(localFile,"");
        }
        Run run = client.getRun(runId);
        System.out.println("Run:\n"+ObjectUtils.toJson(run));
    }
}
