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

    private java.util.Optional<Experiment> getExperimentByName(List<Experiment> exps, String expName) {
        return exps.stream().filter(e -> e.getName().equals(expName)).findFirst();
    }
}
