# MLFlow Java Client

First pass for a Java client for [MLflow](https://mlflow.org) REST API.
See also the MLflow [Python API](https://mlflow.org/docs/latest/python_api/index.html)
and [REST API](https://mlflow.org/docs/latest/rest_api.html).

You will need to have a MLflow tracking server running.

## Requirements

* Java 2.11.8
* Maven
* mlflow 4.0+

## Build
```
mvn package
```

## Java Client API

See [ApiClient.java](src/main/java/com/databricks/mlflow/client/ApiClient.java) 
and [domain objects](src/main/java/com/databricks/mlflow/client/objects).

```
public CreateExperimentResponse createExperiment(String experimentName) 

public Experiment getExperiment(String experimentId) 

public List<Experiment> getExperiments() 


public CreateRunResponse createRun(CreateRunRequest request)

public void updateRun(UpdateRunRequest request)

public GetRunResponse getRun(String runUuid)


public void logParameter(String runUuid, String key, String value)

public void logMetric(String runUuid, String key, double value) 
```

## Sample

[QuickStartDriver.java](src/main/java/com/databricks/mlflow/client/samples/QuickStartDriver.java) does the following:
* Creates new experiment
* Gets experiment details
* Lists experiments
* Creates run 
* Logs some parameters
* Logs some metrics
* Updates finished run
* Gets run details
* Gets experiment details again

```
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
        CreateExperimentResponse expResponse = client.createExperiment("Exp_"+System.currentTimeMillis());
        String experimentId = expResponse.getExperimentId();
        System.out.println("createExperiment: "+expResponse);

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

```

## Run sample

```
mvn package
java -cp target/mlflow-java-1.0-SNAPSHOT.jar com.databricks.mlflow.client.QuickStartDriver.Driver localhost 5000
```

