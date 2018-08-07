package com.databricks.mlflow.client;

import java.util.*;
import org.testng.Assert;
import org.testng.annotations.*;
import com.databricks.mlflow.client.objects.*;

/** 
  Check the results of a Python sckit-learn program run in the test Docker container.
  See $ROOT/docker: sklearn_sample.py and run_all.sh.
*/
public class PythonScikitLearnTest extends BaseTest {
    String expId = "0" ;
    RunInfo run ;
    double auc = 2.0;
    double accuracy_score = 0.9733333333333334;
    double zero_one_loss = 0.026666666666666616;

    @Test
    public void checkExperiment() throws Exception {
        GetExperimentResponse rsp = client.getExperiment(expId);
        List<RunInfo> runs = rsp.getRuns();
        Assert.assertEquals(runs.size(),1);
        run = runs.get(0);
    }

    @Test (dependsOnMethods={"checkExperiment"})
    public void checkRunInfo() throws Exception {
        String runId = run.getRunUuid();
        GetRunResponse rsp = client.getRun(runId);
        RunInfo runInfo = rsp.getInfo();

        List<Param> params = rsp.getData().getParams();
        Assert.assertEquals(params.size(),2);
        assertParam(params,"min_samples_leaf","2");
        assertParam(params,"max_depth","3");

        List<Metric> metrics = rsp.getData().getMetrics();
        Assert.assertEquals(metrics.size(),3);
        assertMetric(metrics,"auc",auc);
        assertMetric(metrics,"accuracy_score",accuracy_score);
        assertMetric(metrics,"zero_one_loss",zero_one_loss);

        Metric m = client.getMetric(runId,"auc");
        Assert.assertEquals(m.getKey(),"auc");
        Assert.assertEquals(m.getValue(),auc);

        metrics = client.getMetricHistory(runId,"auc");
        Assert.assertEquals(metrics.size(),1);
        m = metrics.get(0);
        Assert.assertEquals(m.getKey(),"auc");
        Assert.assertEquals(m.getValue(),auc);
    }

    @Test (dependsOnMethods={"checkExperiment"})
    public void checkArtifacts() throws Exception {
        ListArtifactsResponse rsp = client.listArtifacts(run.getRunUuid(),"");
        List<FileInfo> files = rsp.getFiles();
        Assert.assertEquals(files.size(),3);
        assertFile(files,"confusion_matrix.txt");
        assertFile(files,"classification_report.txt");
        assertFile(files,"model");
    }

    private void assertFile(List<FileInfo> files, String path) {
        Assert.assertTrue(files.stream().filter(e -> e.getPath().equals(path)).findFirst().isPresent());
    }
}
