package com.databricks.mlflow.client;

import java.util.*;
import org.testng.Assert;
import org.testng.annotations.*;
import com.databricks.mlflow.client.objects.*;

public class ApiClientTest extends BaseTest {
    @Test
    public void getCreateExperimentTest() throws Exception {
        String expName = createExperimentName();
        CreateExperimentResponse expCreate = client.createExperiment(expName);
        GetExperimentResponse exp = client.getExperiment(expCreate.getExperimentId());
        Assert.assertEquals(exp.getExperiment().getName(),expName);
    }

    @Test
    public void listExperimentsTest() throws Exception {
        List<Experiment> expsBefore = client.listExperiments();

        String expName = createExperimentName();
        CreateExperimentResponse expCreate = client.createExperiment(expName);

        List<Experiment> exps = client.listExperiments();
        Assert.assertEquals(exps.size(),1+expsBefore.size());

        java.util.Optional<Experiment> opt = getExperimentByName(exps,expName);
        Assert.assertTrue(opt.isPresent());
        Experiment expList = opt.get();
        Assert.assertEquals(expList.getName(),expName);

        Experiment expGet = client.getExperiment(expCreate.getExperimentId()).getExperiment();
        //Assert.assertEquals(expGet,expList); TODO: fails even though all fields are the same

        Assert.assertEquals(expGet.getName(),expList.getName());
        Assert.assertEquals(expGet.getExperimentId(),expList.getExperimentId());
        Assert.assertEquals(expGet.getArtifactLocation(),expList.getArtifactLocation());
    }

    @Test
    public void addGetRun() throws Exception {
        // Create exp 
        String expName = createExperimentName();
        CreateExperimentResponse expCreate = client.createExperiment(expName);
        String experimentId = expCreate.getExperimentId();
    
        // Create run 
        String user = System.getenv("USER");
        long startTime = System.currentTimeMillis();
        String sourceFile = "MyFile.java";
        CreateRunRequest request = new CreateRunRequest(experimentId, "run_for_"+experimentId, "LOCAL", sourceFile, startTime, user);     
        RunInfo runCreated = client.createRun(request);
        String runId = runCreated.getRunUuid();
    
        // Log parameters
        client.logParameter(runId, "min_samples_leaf", "2");
        client.logParameter(runId, "max_depth", "3");
    
        // Log metrics
        client.logMetric(runId, "auc", 1.5);
        client.logMetric(runId, "accuracy_score", 0.667);
        client.logMetric(runId, "zero_one_loss", 0.333);
    
        // Update finished run
        UpdateRunRequest update = new UpdateRunRequest(runId, "FINISHED", startTime+1001);
        client.updateRun(update);
  
        // Assert run from getExperiment
        GetExperimentResponse expResponse = client.getExperiment(expCreate.getExperimentId());
        Experiment exp = expResponse.getExperiment() ;
        Assert.assertEquals(exp.getName(),expName);
        assertRunInfo(expResponse.getRuns().get(0), experimentId, user, sourceFile);
        
        // Assert run from getRun
        GetRunResponse run = client.getRun(runId);
    
        RunInfo runInfo = run.getInfo();
        assertRunInfo(runInfo, experimentId, user, sourceFile);
  
        List<Param> params = run.getData().getParams();
        Assert.assertEquals(params.size(),2);
        assertParam(params,"min_samples_leaf","2");
        assertParam(params,"max_depth","3");
  
        List<Metric> metrics = run.getData().getMetrics();
        Assert.assertEquals(metrics.size(),3);
        assertMetric(metrics,"auc",1.5);
        assertMetric(metrics,"accuracy_score",0.667);
        assertMetric(metrics,"zero_one_loss",0.333);

        Metric m = client.getMetric(runId,"auc");
        Assert.assertEquals(m.getKey(),"auc");
        Assert.assertEquals(Double.compare(m.getValue(),1.5),0);

        metrics = client.getMetricHistory(runId,"auc");
        Assert.assertEquals(metrics.size(),1);
        m = metrics.get(0);
        Assert.assertEquals(m.getKey(),"auc");
        Assert.assertEquals(Double.compare(m.getValue(),1.5),0);
    }

    @Test (expectedExceptions = HttpServerException.class) // TODO: server should throw 406
    public void createExistingExperiment() throws Exception {
        String expName = createExperimentName();
        CreateExperimentResponse expCreate = client.createExperiment(expName);
        GetExperimentResponse exp = client.getExperiment(expCreate.getExperimentId());
        Assert.assertEquals(exp.getExperiment().getName(),expName);

        client.createExperiment(expName);
    }
}
