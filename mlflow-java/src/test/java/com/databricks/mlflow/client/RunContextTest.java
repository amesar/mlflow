package com.databricks.mlflow.client;

import java.nio.file.Path;
import org.testng.Assert;
import org.testng.annotations.*;
import com.databricks.mlflow.client.objects.*;
import static com.databricks.mlflow.client.TestUtils.*;

public class RunContextTest extends BaseTest {
    @Test
    public void createGetRun() throws Exception {
        // Create exp 
        String expName = createExperimentName();
        String expId = client.createExperiment(expName);
    
        // Create run 
        String userId = System.getenv("USER");
        long startTime = System.currentTimeMillis();
        String sourceName = "MyFile.java";
        String runId ;

        //Path localFile = createTempFile();
        //String data = "Lorem ipsum dolor sit amet";
        //writeFile(localFile,data);
        //String artifactPath = "";

        try (RunContext ctx = new RunContext(client, expId, "run_for_"+expId, "LOCAL", sourceName, userId) ) {
            ctx.logParameter("min_samples_leaf", TestShared.min_samples_leaf);
            ctx.logParameter("max_depth", TestShared.max_depth);
            ctx.logMetric("auc", TestShared.auc);
            ctx.logMetric("accuracy_score", TestShared.accuracy_score);
            ctx.logMetric("zero_one_loss", TestShared.zero_one_loss);
            //ctx.logArtifact(localFile.toString(), artifactPath); // XX
            runId = ctx.getRunId();
        }

        // Assert run from getExperiment
        GetExperimentResponse expResponse = client.getExperiment(expId);
        Experiment exp = expResponse.getExperiment() ;
        Assert.assertEquals(exp.getName(),expName);
        assertRunInfo(expResponse.getRuns().get(0), expId, userId, sourceName);
        
        // Assert run from getRun
        Run run = client.getRun(runId);
        RunInfo runInfo = run.getInfo();
        assertRunInfo(runInfo, expId, userId, sourceName);

        // Assert artifact
        //byte [] data2 = client.getArtifact(runId, artifactPath) ;
        //Assert.assertEquals(data,new String(data2));
    }
}
