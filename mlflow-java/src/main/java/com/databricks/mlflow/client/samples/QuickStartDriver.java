package com.databricks.mlflow.client.samples;

import java.util.*;
import com.databricks.mlflow.client.ApiClient;
import com.databricks.mlflow.client.objects.*;

public class QuickStartDriver {
    public static void main(String [] args) throws Exception {
        (new QuickStartDriver()).process(args);
    }

    void process(String [] args) throws Exception {
        if (args.length < 2) {
            System.out.println("ERROR: Missing HOST and PORT");
            System.exit(1);
        }
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        ApiClient client = new ApiClient(host,port);

        System.out.println("====== createExperiment");
        String expName = "Exp_1533266603220";
        String experimentId = client.getOrCreateExperimentId(expName);
/*
        System.out.println("====== createExperiment");
        String expName = "Exp_"+System.currentTimeMillis();
        CreateExperimentResponse expResponse = client.createExperiment(expName);
        String experimentId = expResponse.getExperimentId();
        System.out.println("createExperiment: "+expResponse);
*/

        System.out.println("====== getExperiment");
        GetExperimentResponse exp = client.getExperiment(experimentId);
        System.out.println("getExperiment: "+exp);

        System.out.println("====== listExperiments");
        List<Experiment> exps = client.listExperiments();
        System.out.println("#experiments: "+exps.size());
        exps.forEach(e -> System.out.println("  Exp: "+e));

        createRun(client, experimentId);

        System.out.println("====== getExperiment");
        GetExperimentResponse exp2 = client.getExperiment(experimentId);
        System.out.println("getExperiment: "+exp2);

        Optional<Experiment> ee = client.getExperimentByName(expName);
        System.out.println("getExperimentByName: "+ee);
    }

    void createRun(ApiClient client, String experimentId) throws Exception {
        System.out.println("====== createRun");

        // Create run
        String user = System.getenv("USER");
        long startTime = System.currentTimeMillis();
        String sourceFile = "MyFile.java";
        CreateRunRequest request = new CreateRunRequest(experimentId, "run_for_"+experimentId, "LOCAL", sourceFile, startTime, user);
        RunInfo runCreated = client.createRun(request);
        System.out.println("CreateRun: "+runCreated);
        String runId = runCreated.getRunUuid();

        // Log parameters
        client.logParameter(runId, "min_samples_leaf", "2");
        client.logParameter(runId, "max_depth", "3");

        // Log metrics
        client.logMetric(runId, "auc", 2.12);
        client.logMetric(runId, "accuracy_score", 3.12);
        client.logMetric(runId, "zero_one_loss", 4.12);

        // Update finished run
        UpdateRunRequest update = new UpdateRunRequest(runId, "FINISHED", startTime+1001);
        client.updateRun(update);
    
        // Get run details
        GetRunResponse run = client.getRun(runId);
        System.out.println("GetRun: "+run);
    }
}
