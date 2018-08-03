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
public CreateExperimentResult createExperiment(String experimentName) 

public ExperimentSummary getExperiment(String experimentId) 

public List<ExperimentSummary> getExperiments() 
```

## Usage

See [QuickStartDriver.java](src/main/java/com/databricks/mlflow/client/samples/QuickStartDriver.java).

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
```

## Run API client samples

**Initialize**
```
JAR=target/mlflow-java-1.0-SNAPSHOT.jar
HOST=localhost
PORT=5000
```

### Sample Track Training Run

[QuickStartDriver.java](src/main/java/com/databricks/mlflow/client/samples/QuickStartDriver.java) does the following:
* Creates new experiment
* Gets experiment details
* Lists experiments

```
java -cp $JAR com.databricks.mlflow.client.QuickStartDriver.Driver $HOST $PORT
```

