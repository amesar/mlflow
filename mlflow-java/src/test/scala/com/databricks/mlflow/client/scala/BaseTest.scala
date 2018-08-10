package com.databricks.mlflow.client.scala

import org.testng.Assert._
import org.testng.annotations._
import com.databricks.mlflow.client.ApiClient
import com.databricks.mlflow.client.objects._

class BaseTest() {
  val apiUriDefault = "http://localhost:5001";
  var client: ApiClient = null 

  @BeforeClass
  def BeforeClass() {
      val apiUriProp = sys.env("MLFLOW_TRACKING_URI")
      val apiUri = if (apiUriProp == null) apiUriDefault else apiUriProp
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
