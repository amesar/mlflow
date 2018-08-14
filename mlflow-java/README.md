# MLflow Java Client

Java client for [MLflow](https://mlflow.org) REST API based on 
[Jackson](https://github.com/FasterXML/jackson) JSON serialization.
See also the MLflow [Python API](https://mlflow.org/docs/latest/python_api/index.html)
and [REST API](https://mlflow.org/docs/latest/rest_api.html).

## Requirements

* Java 1.8
* Maven
* [MLflow Tracking Server 0.4.2](https://mlflow.org/docs/latest/tracking.html#running-a-tracking-server) - you will need to run the server

## Build

### Build without tests
```
mvn -DskipTests=true package
```

### Build with tests and Docker

MLflow [tests](src/test/java/com/databricks/mlflow/client) expect a MLflow tracking server to be running on port 5001.

First build and launch the Docker container.
```
docker build -t mlflow-test:latest
docker run -t -d --name mlflow-test -p 5001:5000  mlflow-test:latest
```

The build the jar file.
```
mvn package
```

TODO: Add docker target to Maven.

## Run

To run a simple sample.
```
java -cp target/mlflow-java-client-0.4.2.jar \
  com.databricks.mlflow.client.samples.QuickStartDriver http://localhost:5001
```

## MLflow Versions
This version of the MLflow Java client is built against the 0.4.2 version of the Python server.
If you want to test against new server versons you should change the following:
 * Change the ``<mlflow-version>`` property in [pom.xml](pom.xml).
 * Change ``mlflow==0.4.2`` line in the [Dockerfile](Dockerfile) and rebuild your docker image.


## Java Client API

See [ApiClient.java](src/main/java/com/databricks/mlflow/client/ApiClient.java) 
and [domain objects](src/main/java/com/databricks/mlflow/client/objects).

```
public Experiment getExperiment(String experimentId) 

public List<Experiment> listExperiments() 

public String createExperiment(String experimentName) 


public GetRunResponse getRun(String runUuid)

public CreateRunResponse createRun(CreateRunRequest request)

public void updateRun(String runUuid, String status, long endTime) 


public void logParameter(String runUuid, String key, String value)

public void logMetric(String runUuid, String key, double value) 


public Metric getMetric(String runUuid, String metricKey)

public List<Metric> getMetricHistory(String runUuid, String metricKey)


public ListArtifactsResponse listArtifacts(String runUuid, String path) 

public byte[] getArtifact(String runUuid, String path) 


public SearchResponse search(int [] experimentIds, BaseSearch[] clauses) 

```

## Usage

### Java Usage

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
        String  experimentId = client.createExperiment("Exp_"+System.currentTimeMillis());
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
        client.updateRun(runId, "FINISHED", startTime+1001);
    
        // Get run details
        GetRunResponse run = client.getRun(runId);
        System.out.println("GetRun: "+run);
    }
}

```
### Scala Usage
You can also invoke the MLflow Java client from Scala.
See the short [ScalaDriver.scala](src/main/scala/com/databricks/mlflow/client/samples/ScalaDriver.scala) and
[ApiClientTest.scala](src/test/scala/com/databricks/mlflow/client/scala/ApiClientTest.scala).
```
java -cp target/mlflow-java-client-0.4.2.jar \
  com.databricks.mlflow.client.samples.ScalaDriver http://localhost:5001
```
