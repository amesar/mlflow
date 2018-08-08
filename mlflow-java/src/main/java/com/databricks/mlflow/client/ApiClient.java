package com.databricks.mlflow.client;

import java.util.*;
import org.apache.http.client.utils.URIBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.databricks.mlflow.client.objects.*;

public class ApiClient {
    private String basePath = "api/2.0/preview/mlflow";
    private ObjectMapper mapper = new ObjectMapper();
    private HttpCaller httpCaller ;

    public ApiClient(String baseApiUri) throws Exception {
        this(baseApiUri, false);
    }

    public ApiClient(String baseApiUri, boolean verbose) throws Exception {
        String apiUri = baseApiUri + "/" + basePath;
        httpCaller = new HttpCaller(apiUri,verbose);
    }

    public CreateExperimentResponse createExperiment(String experimentName) throws Exception {
        CreateExperimentRequest request = new CreateExperimentRequest(experimentName);
        String ijson = mapper.writeValueAsString(request);
        String ojson = post("experiments/create",ijson);
        return mapper.readValue(ojson, CreateExperimentResponse.class);
    }

    public List<Experiment> listExperiments() throws Exception {
        return mapper.readValue(get("experiments/list"), ListExperimentsResponse.class).getExperiments();
    }

    public GetExperimentResponse getExperiment(String experimentId) throws Exception {
        URIBuilder builder = httpCaller.makeURIBuilder("experiments/get").setParameter("experiment_id",experimentId);
        return mapper.readValue(httpCaller._get(builder), GetExperimentResponse.class);
    }

    public RunInfo createRun(CreateRunRequest request) throws Exception {
        String ijson = mapper.writeValueAsString(request);
        String ojson = post("runs/create",ijson);
        return mapper.readValue(ojson, CreateRunResponseWrapper.class).getRun().getInfo();
    }

    public void updateRun(UpdateRunRequest request) throws Exception {
        post("runs/update",mapper.writeValueAsString(request));
    }

    public GetRunResponse getRun(String runUuid) throws Exception {
        URIBuilder builder = httpCaller.makeURIBuilder("runs/get").setParameter("run_uuid",runUuid);
        return mapper.readValue(httpCaller._get(builder), GetRunResponseWrapper.class).getRun();
    }

    public void logParameter(String runUuid, String key, String value) throws Exception {
        LogParam request = new LogParam(runUuid, key,value);
        post("runs/log-parameter",mapper.writeValueAsString(request));
    }

    public void logMetric(String runUuid, String key, double value) throws Exception {
        LogMetric request = new LogMetric(runUuid, key, value, ""+System.currentTimeMillis());
        String ijson = mapper.writeValueAsString(request);
        post("runs/log-metric",ijson);
    }

    public Metric getMetric(String runUuid, String metricKey) throws Exception {
        URIBuilder builder = httpCaller.makeURIBuilder("metrics/get")
            .setParameter("run_uuid",runUuid)
            .setParameter("metric_key",metricKey);
        return mapper.readValue(httpCaller._get(builder), GetMetricResponse.class).getMetric();
    }

    public List<Metric> getMetricHistory(String runUuid, String metricKey) throws Exception {
        URIBuilder builder = httpCaller.makeURIBuilder("metrics/get-history")
            .setParameter("run_uuid",runUuid)
            .setParameter("metric_key",metricKey);
        return mapper.readValue(httpCaller._get(builder), GetMetricHistoryResponse.class).getMetrics();
    }

    public ListArtifactsResponse listArtifacts(String runUuid, String path) throws Exception {
        URIBuilder builder = httpCaller.makeURIBuilder("artifacts/list")
            .setParameter("run_uuid",runUuid)
            .setParameter("path",path);
        return mapper.readValue(httpCaller._get(builder), ListArtifactsResponse.class);
    }

    public byte [] getArtifact(String runUuid, String path) throws Exception {
        URIBuilder builder = httpCaller.makeURIBuilder("artifacts/get")
            .setParameter("run_uuid",runUuid)
            .setParameter("path",path);
        return httpCaller._getAsBytes(builder.toString());
    }

    public Optional<Experiment> getExperimentByName(String experimentName) throws Exception {
        return listExperiments().stream().filter(e -> e.getName().equals(experimentName)).findFirst();
    }

    public String getOrCreateExperimentId(String experimentName) throws Exception {
        Optional<Experiment> opt = getExperimentByName(experimentName);
        return opt.isPresent() ? opt.get().getExperimentId() : createExperiment(experimentName).getExperimentId();
    }

    public String get(String path) throws Exception {
        return httpCaller.get(path);
    }

    public String post(String path, String ijson) throws Exception {
        return httpCaller.post(path,ijson);
    }
}
