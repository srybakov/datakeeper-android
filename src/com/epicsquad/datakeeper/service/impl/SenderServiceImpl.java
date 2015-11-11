package com.epicsquad.datakeeper.service.impl;

import com.epicsquad.datakeeper.model.domain.Call;
import com.epicsquad.datakeeper.model.domain.Location;
import com.epicsquad.datakeeper.model.domain.SMS;
import com.epicsquad.datakeeper.service.SenderService;
import com.google.gson.Gson;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class SenderServiceImpl implements SenderService {

    private static final int TIMEOUT_MILLISEC = 5 * 1000;  // = 10 seconds
    private static final String POST_SMS_URL = "";
    private static final String POST_CALL_URL = "";
    private static final String POST_LOCATION_URL = "";

    @Override
    public void sendCall(Call call) throws IOException {
        HttpClient client = getHttpClient();
        HttpPost request = getHttpRequest(POST_CALL_URL, new Gson().toJson(call));
        client.execute(request);
    }

    @Override
    public void sendLocation(Location location) throws IOException {
        HttpClient client = getHttpClient();
        HttpPost request = getHttpRequest(POST_LOCATION_URL, new Gson().toJson(location));
        client.execute(request);
    }

    @Override
    public void sendSMS(SMS sms) throws IOException {
        HttpClient client = getHttpClient();
        HttpPost request = getHttpRequest(POST_SMS_URL, new Gson().toJson(sms));
        client.execute(request);
    }

    private static HttpClient getHttpClient() {
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
        HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
        return new DefaultHttpClient(httpParams);
    }

    private static HttpPost getHttpRequest(String serverUrl, String jsonBody) throws UnsupportedEncodingException {
        HttpPost request = new HttpPost(serverUrl);
        request.setEntity(new ByteArrayEntity(jsonBody.getBytes("UTF8")));
        return request;
    }
}
