package com.databricks.mlflow.client;

import java.util.*;
import org.testng.Assert;
import org.testng.annotations.*;
import com.databricks.mlflow.client.objects.*;
import static com.databricks.mlflow.client.TestUtils.*;

public class MultiThreadedTest extends BaseTest {
    List<String> runIds = Collections.synchronizedList(new ArrayList<String>());
    String expId ;
    String expName ;
    Random random = new Random();
    static final int invocationCount = 10;

    @BeforeClass
    public void beforeClass() throws Exception {
        expName = createExperimentName();
        CreateExperimentResponse expCreate = client.createExperiment(expName);
        expId = expCreate.getExperimentId();
    }

    @Test(threadPoolSize = 3, invocationCount = invocationCount,  timeOut = 10000)
    public void testRunsInParallel() throws Exception {
        long startTime = System.currentTimeMillis();
        String user = "foo";
        String sourceFile = "MyFile.java";

        CreateRunRequest request = new CreateRunRequest(expId, "run_for_"+expId, "LOCAL", sourceFile, startTime, user);   
        RunInfo runCreated = client.createRun(request);
        String runId = runCreated.getRunUuid();
        runIds.add(runId);

        double dval = random.nextDouble() * 100;
        int n = random.nextInt(10);
        for (int j=0 ; j < n ; j++) {
          client.logParameter(runId, "p"+j, ""+(dval+1));
        }
        for (int j=0 ; j < n+1 ; j++) {
          client.logMetric(runId, "m"+j, dval+1);
        }

        // Update finished run
        UpdateRunRequest update = new UpdateRunRequest(runId, "FINISHED", startTime+1001);
        client.updateRun(update);
 
        // Assert run from getExperiment
        GetExperimentResponse expResponse = client.getExperiment(expId);
        Experiment exp = expResponse.getExperiment() ;
        Assert.assertEquals(exp.getName(),expName);
        assertRunInfo(expResponse.getRuns().get(0), expId, user, sourceFile);

        // Assert run from getRun
        GetRunResponse rsp = client.getRun(runId);
        RunInfo runInfo = rsp.getInfo();
        assertRunInfo(runInfo, expId, user, sourceFile);

        // Assert run params
        List<Param> params = rsp.getData().getParams();
        Assert.assertEquals(params.size(),n);
        for (int j=0 ; j < n ; j++) {
            assertParam(params,"p"+j,""+(dval+1));
        }

        // Assert run metrics
        List<Metric> metrics = rsp.getData().getMetrics();
        Assert.assertEquals(metrics.size(),n+1);
        for (int j=0 ; j < n+1 ; j++) {
            assertMetric(metrics,"m"+j,dval+1);
        }
    }

    @Test (dependsOnMethods={"testRunsInParallel"})
    public void checkem() {
        Assert.assertEquals(runIds.size(),invocationCount);
    }
}
