package com.databricks.mlflow.client.samples

import java.io.PrintWriter
import com.databricks.mlflow.client.{ApiClient,RunContext}
import com.databricks.api.proto.mlflow.Service.SourceType

object ScalaQuickStart {
  def main(args: Array[String]) {
    val client = new ApiClient(args(0))

    val expName = "" + System.currentTimeMillis
    println("expName="+expName)
    val expId = client.createExperiment(expName)
    println("expId="+expId)

    var runId = ""
    val sourceName = getClass().getSimpleName()+".scala".replace("\\$","")
    new RunContext(client, expId, "MyScalaRun", SourceType.LOCAL, sourceName, System.getenv("USER")) {
      runId = getRunId()
      println("runId="+runId)
      logParameter("min_samples_leaf", "2")
      logMetric("auc", 2.12F)
      new PrintWriter("info.txt") { write("Some metrics") }
      logArtifact("info.txt","")
    }
    val run = client.getRun(runId)
    println("Run:\n"+run)
  }
}
