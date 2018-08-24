package com.databricks.mlflow.client.store;

import java.util.List;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.testng.Assert;
import org.testng.annotations.*;

public class LocalArtifactRepositoryTest {
    private LocalArtifactRepository repo ;

    @BeforeTest
    public void BeforeTest() throws Exception {
        Path path = createTempDirectory("mlflow_repo_");
        Files.createDirectories(path);
        repo = new LocalArtifactRepository(path.toString());
    }

    @Test 
    public void logArtifact() throws Exception {
        String localFile = "data.txt";
        String data = "Lorem ipsum dolor sit amet";
        writeFile(Paths.get(localFile),data);
        String artifactPath = "runs";
        repo.logArtifact(localFile,artifactPath);
        String odata = readFile(repo.makePath(artifactPath,localFile));
        Assert.assertEquals(odata,data); 

        List<String> artifacts = repo.listArtifacts(artifactPath);
        Assert.assertEquals(artifacts.size(),1); 
        Assert.assertEquals(artifacts.get(0),localFile);
        String apath = repo.downloadArtifacts(artifactPath);
        Assert.assertTrue(Files.exists(Paths.get(apath)));
    }

    @Test 
    public void logArtifacts() throws Exception {
        Path dir = createTempDirectory("mlflow_client_");

        String localFile0 = "data0.txt";
        String data0 = "Lorem ipsum dolor sit amet";
        Path file0 = Paths.get(dir.toString(),localFile0);
        writeFile(file0,data0);

        String localFile1 = "data1.txt";
        String data1 = "Lorem ipsum dolor sit amet 1";
        Path file1 = Paths.get(dir.toString(),localFile1);
        writeFile(file1,data1);

        String artifactPath = "arties";
        repo.logArtifacts(dir.toString(),artifactPath);

        List<String> artifacts = repo.listArtifacts(artifactPath);
        Assert.assertEquals(artifacts.size(),2); 
        Assert.assertEquals(artifacts.get(0),localFile0);
        Assert.assertEquals(artifacts.get(1),localFile1);

        String odata0 = readFile(repo.makePath(artifactPath,localFile0));
        Assert.assertEquals(data0,odata0); 
        String odata1 = readFile(repo.makePath(artifactPath,localFile1));
        Assert.assertEquals(data1,odata1); 
    }

    private Path createTempDirectory(String prefix) throws Exception {
        Path path = Files.createTempDirectory(prefix);
        FileUtils.recursiveDeleteOnShutdownHook(path);
        return path;
    }

    void writeFile(Path path, String data) throws IOException {
        try (PrintWriter w = new PrintWriter(path.toString())) { 
            w.write(data); 
        };
    }

    String readFile(Path path) throws Exception {
        return new Scanner(path).useDelimiter("\\A").next();
    }
}
