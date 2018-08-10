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

    @Test (expectedExceptions = HttpServerException.class) // TODO: server should throw 406
    public void createExistingExperiment() throws Exception {
        String expName = createExperimentName();
        CreateExperimentResponse expCreate = client.createExperiment(expName);
        GetExperimentResponse exp = client.getExperiment(expCreate.getExperimentId());
        Assert.assertEquals(exp.getExperiment().getName(),expName);

        client.createExperiment(expName);
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

	@DataProvider
	public Object[][] searchParameterRequests() {
		return new Object[][]{
			{ "=",  "max_depth", "3" , 1},
            { "!=", "max_depth", "x" , 1},
			{ "=",  "max_depth", "x" , 0},
            { "!=", "max_depth", "3" , 0}
		};
    }

	@Test(dependsOnMethods={"addGetRun"}, dataProvider = "searchParameterRequests")
	public void testSearchParameters(String comparator, String key, String value, int numResults) throws Exception {
        String expectedValue = "3";
        ParameterSearchResponse rsp = client.search(new int[] {0}, new ParameterSearch[] { new ParameterSearch(key,comparator,value) });
        Assert.assertEquals(rsp.getRuns().size(),numResults);
        if (numResults > 0) {
            Run run = rsp.getRuns().get(0);
            List<Param> params = run.getData().getParams();
            assertParam(params,key,expectedValue);
        }
        Assert.assertEquals(rsp.getRuns().size(),numResults);
        if (numResults > 0) {
            Run run = rsp.getRuns().get(0);
            List<Param> params = run.getData().getParams();
            assertParam(params,key,expectedValue);
        }
    }

	@DataProvider
	public Object[][] searchMetricRequests() {
		return new Object[][]{
			{ "=",   "auc", 2 , 1},
			{ ">",   "auc", 1 , 1},
			{ ">=",  "auc", 1 , 1},
			{ ">=",  "auc", 2 , 1},
			{ "<",   "auc", 3 , 1},
			{ "<=",  "auc", 3 , 1},
			{ "<=",  "auc", 2 , 1},
			{ "!=",  "auc", 1 , 1},
			{ "=",   "auc", 9 , 0},
			{ "!=",  "auc", 2 , 0},
			{ ">",   "auc", 9 , 0},
			{ ">=",  "auc", 9 , 0},
			{ "<",   "auc", 1 , 0},
			{ "<=",  "auc", 1 , 0}
		};
    }

    @Test(dependsOnMethods={"addGetRun"}, dataProvider = "searchMetricRequests")
    public void checkSearchMetrics(String comparator, String key, double value, int numResults) throws Exception {
        double expectedValue = 2;
        MetricSearchResponse rsp = client.search(new int[] {0}, new MetricSearch[] { new MetricSearch(key,comparator,value) });
        Assert.assertEquals(rsp.getRuns().size(),numResults);
        if (numResults > 0) {
            Run run = rsp.getRuns().get(0);
            List<Metric> metrics = run.getData().getMetrics();
            assertMetric(metrics,key,expectedValue);
        }
    }
}
