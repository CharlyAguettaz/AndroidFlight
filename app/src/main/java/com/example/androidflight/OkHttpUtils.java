package com.example.androidflight;

import android.util.Log;

import java.util.Arrays;
import java.util.List;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpUtils {


    public static Response sendGetOkHttpRequest(String url, String token) throws Exception {
        Log.w("tag","url: "+ url);

        OkHttpClient client = new OkHttpClient();
        // OkHttpClient client = AppFonctions.getUnsafeOkHttpClient();

        Request request= new Request.Builder().url(url)
                .addHeader("Authorization", "Bearer " + token)
                .get()
                .build();
        Response response = client.newCall(request).execute();


        return response;
    }

    public static Response sendPostOkHttpRequest(String json, String url) throws Exception {

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), json);

        Request request = new Request.Builder().url(url)
                .post(body)
                .addHeader("Content-Type","application/json")
                .build();

        Response response = client.newCall(request).execute();

        return response;
    }
}
