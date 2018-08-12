package com.databricks.mlflow.client.scala

import scala.collection.JavaConversions._
import org.testng.annotations._
import org.testng.Assert._
import com.databricks.mlflow.client.objects._
import com.databricks.mlflow.client.TestShared

class ApiClientTest extends BaseTest {
  var runId: String = _
  var run: Run = _

  @Test
  def getCreateExperimentTest() {
    val expName = createExperimentName()
    val expCreate = client.createExperiment(expName)
    val exp = client.getExperiment(expCreate.getExperimentId())
    assertEquals(exp.getExperiment().getName(),expName)
  }

  @Test
  def listExperimentsTest() {
    val expsBefore = client.listExperiments()

    val expName = createExperimentName()
    val expCreate = client.createExperiment(expName)

    val exps = client.listExperiments()
    assertEquals(exps.size(),1+expsBefore.size());

    val opt = getExperimentByName(exps,expName)
    assertFalse(opt == None)
    val expList = opt.get

    val expGet = client.getExperiment(expCreate.getExperimentId()).getExperiment();
    equals(expGet,expList)
  }

  @Test
  def addGetRun() {
    // Create exp 
    val expName = createExperimentName()
    val expCreate = client.createExperiment(expName)
    val experimentId = expCreate.getExperimentId()

    // Create run 
    val user = System.getenv("USER");
    val startTime = System.currentTimeMillis();
    val sourceFile = "MyFile.java";
    val request = new CreateRunRequest(experimentId, "run_for_"+experimentId, "LOCAL", sourceFile, startTime, user);     
    val runCreated = client.createRun(request);
    //val runId = runCreated.getRunUuid();
    runId = runCreated.getRunUuid();

    // Log parameters
    client.logParameter(runId, "min_samples_leaf", "2");
    client.logParameter(runId, "max_depth", "3");

    // Log metrics
    client.logMetric(runId, "auc", 2.12);
    client.logMetric(runId, "accuracy_score", 3.12);
    client.logMetric(runId, "zero_one_loss", 4.12);

    // Update finished run
    val update = new UpdateRunRequest(runId, "FINISHED", startTime+1001);
    client.updateRun(update);
    
    // Get run details
    //val run = client.getRun(runId);
    run = client.getRun(runId)

    val runInfo = run.getInfo()
    assertEquals(runInfo.getExperimentId(),experimentId)
    assertEquals(runInfo.getUserId(),user)
    assertEquals(runInfo.getExperimentId(),experimentId)
    assertEquals(runInfo.getSourceName(),sourceFile)
  }

  @Test(dependsOnMethods = Array("addGetRun"))
  def checkParamsAndMetrics() {
    val params = run.getData().getParams()
    val metrics = run.getData().getMetrics()
    assertEquals(params.size,2)
    assertEquals(metrics.size,3)
    //TestShared.assertParamsAndMetrics(client, client.getRun(runId), runId);
  }

  @Test(dependsOnMethods = Array("addGetRun"))
  def checkSearch() {
    import com.databricks.mlflow.client.TestUtils

    val rsp = client.search(Array(0), Array(new ParameterSearch("max_depth","=","3")))
    assertEquals(rsp.getRuns().size(),1)
    val run = rsp.getRuns().get(0)
    val params = run.getData().getParams()
    TestUtils.assertParam(params,"max_depth","3")
  }

  def getExperimentByName(exps: Seq[Experiment], experimentName: String) = {
    exps find (_.getName == experimentName)
  }

}