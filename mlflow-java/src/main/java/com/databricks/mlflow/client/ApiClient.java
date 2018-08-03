package com.databricks.mlflow.client;

import java.util.*;
import org.apache.http.HttpHost;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.databricks.mlflow.client.objects.*;

public class ApiClient {
    private String host ;
    private String apiUri ;
    private int port ;
    private String basePath = "api/2.0/preview/mlflow";
    private HttpHost httpHost ;
    private DefaultHttpClient httpclient = new DefaultHttpClient();
    private ObjectMapper mapper = new ObjectMapper();

    public ApiClient(String host, int port) {
        this.host = host;
        this.port = port;
        this.basePath = "api/2.0/preview/mlflow";
        httpHost = new HttpHost(host, port, "http");
    }

    public CreateExperimentResult createExperiment(String experimentName) throws Exception {
        CreateExperimentRequest request = new CreateExperimentRequest();
        request.setName(experimentName);
        String ijson = mapper.writeValueAsString(request);
        String ojson = post("experiments/create",ijson);
        return mapper.readValue(ojson, CreateExperimentResult.class);
    }

    public List<ExperimentSummary> listExperiments() throws Exception {
        String str = get("experiments/list");
        return mapper.readValue(str, ListExperimentsResultWrapper.class).getExperiments();
    }

    public ExperimentSummary getExperiment(String experimentId) throws Exception {
        String path = "experiments/get?experiment_id="+experimentId;
        String str = get(path);
        return mapper.readValue(str, GetExperimentResultWrapper.class).getExperiment();
    }

    String get(String path) throws Exception {
        String fpath = basePath + "/" + path; 
        System.out.println("ApiClient.get: path: "+fpath);
        HttpGet request = new HttpGet(fpath);
        HttpResponse response = httpclient.execute(httpHost, request);
        HttpEntity entity = response.getEntity();
        String ojson = EntityUtils.toString(entity);
        System.out.println("ApiClient.get: ojson: "+ojson);
        return ojson;
    }

    String post(String path, String ijson) throws Exception {
        String fpath = basePath + "/" + path; 
        System.out.println("ApiClient.post: path: "+fpath);
        StringEntity ientity = new StringEntity(ijson);
        System.out.println("ApiClient.post: ijson: "+ijson);

        HttpPost request = new HttpPost(fpath);
        request.setEntity(ientity);
        HttpResponse response = httpclient.execute(httpHost, request);
        HttpEntity oentity = response.getEntity();
        String ojson = EntityUtils.toString(oentity);
        System.out.println("ApiClient.post: ojson: "+ojson);
        return ojson;
    }
}
