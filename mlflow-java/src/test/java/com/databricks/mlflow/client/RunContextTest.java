package com.databricks.mlflow.client;

import java.nio.file.Path;
import org.testng.Assert;
import org.testng.annotations.*;
import com.databricks.api.proto.mlflow.Service.*;
import static com.databricks.mlflow.client.TestUtils.*;

public class RunContextTest extends BaseTest {
    private boolean testLogArtifact = false ; // TODO: If MLflow server's artifact path is local disk (as in test Docker) then this fails

    @Test
    public void createGetRun_EmptyArtifactPath() throws Exception {
        _createGetRun("");
    }

    @Test
    public void createGetRun_NonEmptyArtifactPath() throws Exception {
        _createGetRun("a/b");
    }

    public void _createGetRun(String artifactPath) throws Exception {
        // Create exp 
        String expName = createExperimentName();
        long expId = client.createExperiment(expName);
    
        // Create run 
        String userId = System.getenv("USER");
        long startTime = System.currentTimeMillis();
        String sourceName = "MyFile.java";
        String runId ;

        String data = "Lorem ipsum dolor sit amet";
        Path localFile = createTempFile();
        if (testLogArtifact) {
            writeFile(localFile,data);
        }

        try (RunContext ctx = new RunContext(client, expId, "run_for_"+expId, SourceType.LOCAL, sourceName, userId) ) {
            ctx.logParameter("min_samples_leaf", TestShared.min_samples_leaf);
            ctx.logParameter("max_depth", TestShared.max_depth);
            ctx.logMetric("auc", TestShared.auc);
            ctx.logMetric("accuracy_score", TestShared.accuracy_score);
            ctx.logMetric("zero_one_loss", TestShared.zero_one_loss);
            if (testLogArtifact) {
                ctx.logArtifact(localFile.toString(), artifactPath);
            }
            runId = ctx.getRunId();
        }

        // Assert run from getExperiment
        GetExperiment.Response expResponse = client.getExperiment(expId);

        Experiment exp = expResponse.getExperiment() ;
        Assert.assertEquals(exp.getName(),expName);
        assertRunInfo(expResponse.getRunsList().get(0), expId, userId, sourceName);
        
        // Assert run from getRun
        Run run = client.getRun(runId);
        RunInfo runInfo = run.getInfo();
        assertRunInfo(runInfo, expId, userId, sourceName);

        // Assert artifact
        if (testLogArtifact) {
            byte [] data2 = client.getArtifact(runId, artifactPath) ;
            Assert.assertEquals(data,new String(data2));
        }
    }
}
