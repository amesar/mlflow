# MLflow Java Client

First pass for a Java client for [MLflow](https://mlflow.org) REST API.
See also the MLflow [Python API](https://mlflow.org/docs/latest/python_api/index.html)
and [REST API](https://mlflow.org/docs/latest/rest_api.html).

## Requirements

* Java 1.8
* Maven
* [MLflow Tracking Server](https://mlflow.org/docs/latest/tracking.html#running-a-tracking-server) - you will need to launch a server

## Build and Run
```
mvn -DskipTests=true package
java -cp target/mlflow-java-1.0-SNAPSHOT.jar com.databricks.mlflow.client.samples.QuickStartDriver http://localhost:5000
```

### Scala Client Usage
You can also invoke the MLflow Java client from Scala.
See the preliminary [ScalaDriver.scala](src/main/scala/com/databricks/mlflow/client/samples/ScalaDriver.scala) and
[ApiClientTest.scala](src/test/scala/com/databricks/mlflow/client/scala/ApiClientTest.scala).
```
java -cp target/mlflow-java-1.0-SNAPSHOT.jar com.databricks.mlflow.client.samples.ScalaDriver http://localhost:5000
```

### Tests

MLflow [tests](src/test/java/com/databricks/mlflow/client) expect a MLflow tracking server to be running on port 5001.
If you don't wish to run tests, build with ``skipTests`` property as above.
Otherwise, run an MLflow tracking server on port 5001 and build with ``mvn package``.

If you wish to run the tests, you can run the Docker container that launches an MLflow tracking server.

* Build image: ``docker build -t mlflow-test:latest``
* Run container: ``docker run -t -d --name mlflow-test -p 5001:5000  mlflow-test:latest``

## Java Client API

See [ApiClient.java](src/main/java/com/databricks/mlflow/client/ApiClient.java) 
and [domain objects](src/main/java/com/databricks/mlflow/client/objects).

```
public CreateExperimentResponse createExperiment(String experimentName) 

public Experiment getExperiment(String experimentId) 

public List<Experiment> listExperiments() 


public CreateRunResponse createRun(CreateRunRequest request)

public void updateRun(UpdateRunRequest request)

public GetRunResponse getRun(String runUuid)


public void logParameter(String runUuid, String key, String value)

public void logMetric(String runUuid, String key, double value) 

public Metric getMetric(String runUuid, String metricKey)

public List<Metric> getMetricHistory(String runUuid, String metricKey)


public ListArtifactsResponse listArtifacts(String runUuid, String path) 

public byte[] getArtifact(String runUuid, String path) 
```

## Usage

See [ApiClientTest.java](src/test/java/com/databricks/mlflow/client/ApiClientTest.java) and
 [QuickStartDriver.java](src/main/java/com/databricks/mlflow/client/samples/QuickStartDriver.java) for usage.

The latter does the following:
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
        String apiUri = args[0];
        boolean verbose = args.length < 2 ? false : Boolean.parseBoolean("true");
        ApiClient client = new ApiClient(apiUri, verbose);

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
