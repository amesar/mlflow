package com.databricks.mlflow.client;

import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;
import org.apache.log4j.Level;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
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
    private HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
    private HttpClient httpClient ;
    private String token;

    public HttpCaller(String apiUri) throws Exception {
        this(apiUri, null);
    }

    public HttpCaller(String apiUri, String token) throws Exception {
        this.apiUri = apiUri;
        this.token = token;
        logger.info("apiUri: "+apiUri+" token="+token);
        httpClient = httpClientBuilder.build();
    }

    public void setVerbose(boolean verbose) {
        if (verbose) {
            LogManager.getLogger("com.databricks").setLevel(Level.DEBUG);
        }
    }

    public String get(String path) throws Exception {
        return _get(makeUri(path));
    }

    public String _get(URIBuilder uriBuilder) throws Exception {
        return _get(uriBuilder.toString());
    }

    public String _get(String uri) throws Exception {
        logger.debug("uri: "+uri);
        HttpGet request = new HttpGet(uri);
        doAuthentication(request);
        HttpResponse response = httpClient.execute(request);
        checkError(response);
        HttpEntity entity = response.getEntity();
        String ojson = EntityUtils.toString(entity);
        logger.debug("response: "+ojson);
        return ojson;
    }

    public byte[] _getAsBytes(String uri) throws Exception {
        logger.debug("uri: "+uri);
        HttpGet request = new HttpGet(uri);
        doAuthentication(request);
        HttpResponse response = httpClient.execute(request);
        checkError(response);
        HttpEntity entity = response.getEntity();
        byte[] bytes = EntityUtils.toByteArray(entity);
        logger.debug("response: #bytes="+bytes.length);
        return bytes;
    }

    public String post(String path, String ijson) throws Exception {
        String uri = makeUri(path);
        logger.debug("uri: "+uri);
        StringEntity ientity = new StringEntity(ijson);
        logger.debug("request: "+ijson);

        HttpPost request = new HttpPost(uri);
        doAuthentication(request); 
        request.setEntity(ientity);
        HttpResponse response = httpClient.execute(request);
        HttpEntity oentity = response.getEntity();
        checkError(response);
        String ojson = EntityUtils.toString(oentity);
        logger.debug("response: "+ojson);
        return ojson;
    }

    private void doAuthentication(HttpRequestBase request) {
        if (token != null && !token.isEmpty()) {
            request.setHeader("Authorization","Bearer "+token);
        }
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
