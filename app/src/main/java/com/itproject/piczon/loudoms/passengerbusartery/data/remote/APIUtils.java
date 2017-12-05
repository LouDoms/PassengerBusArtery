package com.itproject.piczon.loudoms.passengerbusartery.data.remote;

/**
 * Created by LouDoms on 11/30/2017.
 */

public class APIUtils {
    private APIUtils() {}

    public static final String BASE_URL = "http://192.168.0.120:1337/";
    public static APIService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }
}
