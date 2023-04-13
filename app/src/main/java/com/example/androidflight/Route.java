package com.example.androidflight;

import android.util.Pair;

import com.google.gson.Gson;

import okhttp3.Response;

public class Route {

    public static void postAction(FlightAction flightAction) throws Exception {
        Gson gson = new Gson();
        Response response = OkHttpUtils.sendPostOkHttpRequest(gson.toJson(flightAction), "http://monappli.ovh:58015/pilotageApp/action");
        String responseJson = response.body().string();
        System.out.println(responseJson);
    }

    public static class FlightAction {
        public String value;

        public FlightAction (String value) {
            this.value = value;
        }
    }

}
