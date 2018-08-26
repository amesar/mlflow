# MLflow Java Client

Java client for [MLflow](https://mlflow.org) REST API based on 
[Jackson](https://github.com/FasterXML/jackson) JSON serialization.
See also the MLflow [Python API](https://mlflow.org/docs/latest/python_api/index.html)
and [REST API](https://mlflow.org/docs/latest/rest_api.html).

## Requirements

* Java 1.8
* Maven
* Run the [MLflow Tracking Server 0.4.2](https://mlflow.org/docs/latest/tracking.html#running-a-tracking-server)

## Build

### Build without tests
```
mvn -DskipTests=true package
```

### Build with tests and Docker

MLflow [tests](src/test/java/com/databricks/mlflow/client) expect a MLflow tracking server to be running on port 5001.
You can override this by setting the MLFLOW_TRACKING_URI environment variable to a value such as ``http://localhost:5002``.

First build and launch the Docker container.
```
docker build -t mlflow-test:latest
docker run -t -d --name mlflow-test -p 5001:5000  mlflow-test:latest
```

The build the jar file.
```
mvn package
```

The test report can be found at [target/surefire-reports/index.html](target/surefire-reports/index.html).

TODO: Add docker target to Maven.

## Run

To run a simple sample.
```
java -cp target/mlflow-java-client-jackson-0.4.2.jar \
  com.databricks.mlflow.client.samples.QuickStart http://localhost:5001
```

## MLflow Versions
This version of the MLflow Java client is built against the 0.4.2 version of the Python server.
If you want to test against new server versons do the following:
 * Change ``mlflow==0.4.2`` line in the [Dockerfile](Dockerfile) and rebuild your docker image.
 * Change the ``<mlflow-version>`` property in [pom.xml](pom.xml) so you create a jar with the correct version.

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

Simple example [QuickStart.java](src/main/java/com/databricks/mlflow/client/samples/QuickStart.java)
```
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
```
For more complete examples of API coverage see:
* Sample main program [Sampler.java](src/main/java/com/databricks/mlflow/client/samples/Sampler.java)
* [tests](src/test/java/com/databricks/mlflow/client) such as [ApiClientTest.java](src/test/java/com/databricks/mlflow/client/ApiClientTest.java)

### Scala Usage
You can also invoke the MLflow Java client from Scala.
See the short [ScalaDriver.scala](src/main/scala/com/databricks/mlflow/client/samples/ScalaDriver.scala) and
[ApiClientTest.scala](src/test/scala/com/databricks/mlflow/client/scala/ApiClientTest.scala).
```
java -cp target/mlflow-java-client-jackson-0.4.2.jar \
  com.databricks.mlflow.client.samples.ScalaDriver http://localhost:5001
```
