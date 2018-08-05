package com.databricks.mlflow.client;

import java.util.*;
import java.net.URL;
import org.apache.http.HttpHost;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.databricks.mlflow.client.objects.*;

public class ApiClient {
    private String basePath = "api/2.0/preview/mlflow";
    private HttpHost httpHost ;
    private HttpClient httpClient = HttpClientBuilder.create().build();
    private ObjectMapper mapper = new ObjectMapper();

    public ApiClient(String uri) throws Exception {
        URL url = new URL(uri);
        httpHost = new HttpHost(url.getHost(), url.getPort(), url.getProtocol());
    }

    public CreateExperimentResponse createExperiment(String experimentName) throws Exception {
        CreateExperimentRequest request = new CreateExperimentRequest();
        request.setName(experimentName);
        String ijson = mapper.writeValueAsString(request);
        String ojson = post("experiments/create",ijson);
        return mapper.readValue(ojson, CreateExperimentResponse.class);
    }

    public List<Experiment> listExperiments() throws Exception {
        String ijson = get("experiments/list");
        return mapper.readValue(ijson, ListExperimentsResponse.class).getExperiments();
    }

    public GetExperimentResponse getExperiment(String experimentId) throws Exception {
        String path = "experiments/get?experiment_id="+experimentId;
        String ijson = get(path);
        return mapper.readValue(ijson, GetExperimentResponse.class);
    }

    public RunInfo createRun(CreateRunRequest request) throws Exception {
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

    public Optional<Experiment> getExperimentByName(String experimentName) throws Exception {
        return listExperiments().stream().filter(e -> e.getName().equals(experimentName)).findFirst();
    }

    public String getOrCreateExperimentId(String experimentName) throws Exception {
        Optional<Experiment> opt = getExperimentByName(experimentName);
        return opt.isPresent() ? opt.get().getExperimentId() : createExperiment(experimentName).getExperimentId();
    }

    String get(String path) throws Exception {
        String fpath = basePath + "/" + path; 
        System.out.println("ApiClient.get: path: "+fpath);
        HttpGet request = new HttpGet(fpath);
        HttpResponse response = httpClient.execute(httpHost, request);
        checkError(response);
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
        HttpResponse response = httpClient.execute(httpHost, request);
        HttpEntity oentity = response.getEntity();
        checkError(response);
        String ojson = EntityUtils.toString(oentity);
        System.out.println("ApiClient.post: ojson: "+ojson);
        return ojson;
    }

    private void checkError(HttpResponse response) throws Exception {
        int statusCode = response.getStatusLine().getStatusCode();
        String reasonPhrase = response.getStatusLine().getReasonPhrase();
        if (isError(statusCode)) {
            String bodyMessage = EntityUtils.toString(response.getEntity());
            if (statusCode >= 400 && statusCode <= 499) {
                throw new HttpClientException(statusCode,reasonPhrase,bodyMessage);
            }
            if (statusCode >= 500 && statusCode <= 599) {
                throw new HttpServerException(statusCode,reasonPhrase,bodyMessage);
            }
            throw new HttpException(statusCode,reasonPhrase,bodyMessage);
         }
    }

    private boolean isError(int statusCode) {
        return statusCode < 200 || statusCode > 299 ;
    }
}
