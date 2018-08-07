package com.databricks.mlflow.client;

import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;
import org.apache.log4j.Level;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.http.client.utils.URIBuilder;

public class HttpCaller {
    private static final Logger logger = Logger.getLogger(HttpCaller.class);
    private String apiUri ;
    private String basePath = "api/2.0/preview/mlflow";
    private HttpClient httpClient = HttpClientBuilder.create().build();

    public HttpCaller(String apiUri) throws Exception {
        this(apiUri, false);
    }

    public HttpCaller(String apiUri, boolean verbose) throws Exception {
        this.apiUri = apiUri;
        if (verbose) {
            LogManager.getLogger("com.databricks").setLevel(Level.DEBUG);
        }
        logger.info("apiUri: "+apiUri);
    }

    public String get(String path) throws Exception {
        return _get(makeUri(path));
    }

    public String _get(URIBuilder uriBuilder) throws Exception {
        return _get(uriBuilder.toString());
    }

    public String _get(String uri) throws Exception {
        logger.info("uri: "+uri);
        HttpGet request = new HttpGet(uri);
        HttpResponse response = httpClient.execute(request);
        checkError(response);
        HttpEntity entity = response.getEntity();
        String ojson = EntityUtils.toString(entity);
        logger.info("response: "+ojson);
        return ojson;
    }

    public String post(String path, String ijson) throws Exception {
        String uri = makeUri(path);
        logger.info("uri: "+uri);
        StringEntity ientity = new StringEntity(ijson);
        logger.info("request: "+ijson);

        HttpPost request = new HttpPost(uri);
        request.setEntity(ientity);
        HttpResponse response = httpClient.execute(request);
        HttpEntity oentity = response.getEntity();
        checkError(response);
        String ojson = EntityUtils.toString(oentity);
        logger.info("response: "+ojson);
        return ojson;
    }

    private String makeUri(String path) {
        return apiUri + "/" + path; 
    }

    public URIBuilder makeURIBuilder(String path) throws Exception {
        return new URIBuilder(apiUri+"/"+path);
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
