package com.databricks.mlflow.client;

import java.util.*;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
import org.testng.Assert;
import com.databricks.mlflow.client.objects.*;

public class TestUtils {

    final static double EPSILON = 0.0001;

    public static boolean equals(double a, double b){
        return a == b ? true : Math.abs(a - b) < EPSILON;
    }

    public static void assertRunInfo(RunInfo runInfo, String experimentId, String user, String sourceName) {
        Assert.assertEquals(runInfo.getExperimentId(),experimentId);
        Assert.assertEquals(runInfo.getUserId(),user);
        Assert.assertEquals(runInfo.getSourceName(),sourceName);
    }

    public static void assertParam(List<Param> params, String key, String value) {
        Assert.assertTrue(params.stream().filter(e -> e.getKey().equals(key) && e.getValue().equals(value)).findFirst().isPresent());
    }

    public static void assertMetric(List<Metric> metrics, String key, Double value) {
        Assert.assertTrue(metrics.stream().filter(e -> e.getKey().equals(key) && equals(e.getValue(),value)).findFirst().isPresent());
    }

    public static java.util.Optional<Experiment> getExperimentByName(List<Experiment> exps, String expName) {
        return exps.stream().filter(e -> e.getName().equals(expName)).findFirst();
    }

    public static String createExperimentName() {
        return "TestExp_" + UUID.randomUUID().toString();
    }

    public static Path createTempFile() throws Exception {
        Path path = Files.createTempFile("mlflow_",null);
        path.toFile().deleteOnExit();
        return path;
    }

    public static Path createTempDirectory(String prefix) throws Exception {
        Path path = Files.createTempDirectory(prefix);
        FileUtils.recursiveDeleteOnShutdownHook(path);
        return path;
    }

    public static void writeFile(Path path, String data) throws IOException {
        try (PrintWriter w = new PrintWriter(path.toString())) {
            w.write(data);
        };
    }

    public static String readFile(Path path) throws Exception {
        return new Scanner(path).useDelimiter("\\A").next();
    }
}
