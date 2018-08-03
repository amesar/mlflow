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
        CreateExperimentResult exp = client.createExperiment("Exp_"+System.currentTimeMillis());
        System.out.println("CreateExperimentResult: "+exp);

        System.out.println("====== getExperiment");
        ExperimentSummary exp2 = client.getExperiment(exp.getExperimentId());
        System.out.println("ExperimentSummary: "+exp2);

        System.out.println("====== listExperiments");
        List<ExperimentSummary> exps = client.listExperiments();
        System.out.println("#experiments: "+exps.size());
        exps.forEach(e -> System.out.println("  Exp: "+e));
    }
}
