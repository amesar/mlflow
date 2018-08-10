package com.databricks.mlflow.client.scala

import org.testng.Assert._
import org.testng.annotations._
import com.databricks.mlflow.client.ApiClient
import com.databricks.mlflow.client.objects._
import scala.util.Properties;

class BaseTest() {
  val apiUriDefault = "http://localhost:5001";
  var client: ApiClient = null 

  @BeforeClass
  def BeforeClass() {
      val apiUri = Properties.envOrElse("MLFLOW_TRACKING_URI",apiUriDefault)
      client = new ApiClient(apiUri);
  }

  def createExperimentName() = "TestScala_"+System.currentTimeMillis.toString

  def equals(exp1: Experiment, exp2: Experiment) {
    //assertEquals(expGet,expList); // TODO: fails even though all fields are the same
    assertEquals(exp1.getName(),exp2.getName())
    assertEquals(exp1.getExperimentId(),exp2.getExperimentId())
    assertEquals(exp1.getArtifactLocation(),exp2.getArtifactLocation())
  }
}
