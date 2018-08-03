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

    public CreateExperimentResponse createExperiment(String experimentName) throws Exception {
        CreateExperimentRequest request = new CreateExperimentRequest();
        request.setName(experimentName);
        String ijson = mapper.writeValueAsString(request);
        String ojson = post("experiments/create",ijson);
        return mapper.readValue(ojson, CreateExperimentResponse.class);
    }

    public List<ExperimentSummary> listExperiments() throws Exception {
        String ijson = get("experiments/list");
        return mapper.readValue(ijson, ListExperimentsResponseWrapper.class).getExperiments();
    }

    public GetExperimentResponse getExperiment(String experimentId) throws Exception {
        String path = "experiments/get?experiment_id="+experimentId;
        String ijson = get(path);
        return mapper.readValue(ijson, GetExperimentResponse.class);
    }

    public CreateRunResponse createRun(CreateRunRequest request) throws Exception {
        String ijson = mapper.writeValueAsString(request);
        String ojson = post("runs/create",ijson);
        return mapper.readValue(ojson, CreateRunResponseWrapper.class).getRun().getInfo();
    }

    public void updateRun(UpdateRunRequest request) throws Exception {
        String ijson = mapper.writeValueAsString(request);
        post("runs/update",ijson);
    }

    public GetRunResponse getRun(String runUuid) throws Exception {
        String path = "runs/get?run_uuid="+runUuid;
        String ojson = get(path);
        return mapper.readValue(ojson, GetRunResponseWrapper.class).getRun();
    }

    public void logParameter(String runUuid, String key, String value) throws Exception {
        LogParam request = new LogParam(runUuid, key,value);
        String ijson = mapper.writeValueAsString(request);
        post("runs/log-parameter",ijson);
    }

    public void logMetric(String runUuid, String key, double value) throws Exception {
        LogMetric request = new LogMetric(runUuid, key, value, ""+System.currentTimeMillis());
        String ijson = mapper.writeValueAsString(request);
        post("runs/log-metric",ijson);
    }

    public Optional<ExperimentSummary> getExperimentByName(String experimentName) throws Exception {
        return listExperiments().stream().filter(e -> e.getName().equals(experimentName)).findFirst();
    }

    public String getOrCreateExperimentId(String experimentName) throws Exception {
        Optional<ExperimentSummary> opt = getExperimentByName(experimentName);
        return opt.isPresent() ? opt.get().getExperimentId() : createExperiment(experimentName).getExperimentId();
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
