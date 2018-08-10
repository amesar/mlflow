package com.databricks.mlflow.client;

import java.util.*;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.*;
import com.databricks.mlflow.client.objects.*;
import static com.databricks.mlflow.client.TestUtils.*;

public class ApiClientTest extends BaseTest {
    private static final Logger logger = Logger.getLogger(ApiClientTest.class);
    String runId ;

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
        runId = runCreated.getRunUuid();
        logger.debug("runId="+runId);
    
        // Log parameters
        client.logParameter(runId, "min_samples_leaf", TestShared.min_samples_leaf);
        client.logParameter(runId, "max_depth", TestShared.max_depth);
    
        // Log metrics
        client.logMetric(runId, "auc", TestShared.auc);
        client.logMetric(runId, "accuracy_score", TestShared.accuracy_score);
        client.logMetric(runId, "zero_one_loss", TestShared.zero_one_loss);
    
        // Update finished run
        UpdateRunRequest update = new UpdateRunRequest(runId, "FINISHED", startTime+1001);
        client.updateRun(update);
  
        // Assert run from getExperiment
        GetExperimentResponse expResponse = client.getExperiment(expCreate.getExperimentId());
        Experiment exp = expResponse.getExperiment() ;
        Assert.assertEquals(exp.getName(),expName);
        assertRunInfo(expResponse.getRuns().get(0), experimentId, user, sourceFile);
        
        // Assert run from getRun
        Run run = client.getRun(runId);
    
        RunInfo runInfo = run.getInfo();
        assertRunInfo(runInfo, experimentId, user, sourceFile);
    }

    @Test (dependsOnMethods={"addGetRun"})
    public void checkParamsAndMetrics() throws Exception {
        TestShared.assertParamsAndMetrics(client, client.getRun(runId), runId);
    }

    @Test (dependsOnMethods={"addGetRun"})
    public void checkSearchParameters() throws Exception {
        String key = "max_depth";
        String value = "3";
        StringClause clause = new StringClause("=",value);
        ParameterSearchExpression expression = new ParameterSearchExpression(clause,key);
        List<ParameterSearchExpressionWrapper> expressions = Collections.singletonList(new ParameterSearchExpressionWrapper(expression));
        List<Integer> expIds = Collections.singletonList(0);
        ParameterSearchRequest search = new ParameterSearchRequest(expIds,expressions);

        ParameterSearchResponse rsp = client.search(search);
        Assert.assertEquals(rsp.getRuns().size(),1);
        Run run = rsp.getRuns().get(0);
        List<Param> params = run.getData().getParams();
        assertParam(params,key,value);
    }

    @Test (dependsOnMethods={"addGetRun"})
    public void checkSearchMetrics() throws Exception {
        String key = "auc";
        double value = 2;
        FloatClause clause = new FloatClause("=",value);
        MetricSearchExpression expression = new MetricSearchExpression(clause,key);
        List<MetricSearchExpressionWrapper> expressions = Collections.singletonList(new MetricSearchExpressionWrapper(expression));
        List<Integer> expIds = Collections.singletonList(0);
        MetricSearchRequest search = new MetricSearchRequest(expIds,expressions);

        MetricSearchResponse rsp = client.search(search);
        Assert.assertEquals(rsp.getRuns().size(),1);
        Run run = rsp.getRuns().get(0);
        List<Metric> metrics = run.getData().getMetrics();
        assertMetric(metrics,key,value);
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
