package com.itproject.piczon.loudoms.passengerbusartery.data.remote;

import com.itproject.piczon.loudoms.passengerbusartery.data.model.Passenger;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by LouDoms on 11/30/2017.
 */

public interface APIService {
    @POST("/passengers")
    @FormUrlEncoded
    Call<Passenger> savePost(@Field("name") String name,
                             @Field("username") String username,
                             @Field("password") String password,
                             @Field("gender") String gender,
                             @Field("age") Integer age,
                             @Field("balance") Integer balance,
                             @Field("busNumber") String busNumber,
                             @Field("busType") String busType,
                             @Field("inTransit") Integer inTransit,
                             @Field("type") String type,
                             @Field("tagId") Integer tagId,
                             @Field("destination") String destination,
                             @Field("numberOfSeats") Integer numberOfSeats,
                             @Field("longitude") Double longitude,
                             @Field("latitude") Double latitude);


    @GET("/sessions")
    Call<Passenger> getUserLogin(@Query("username") String username,
                                 @Query("password") String password);


    @POST("/sessions")
    @FormUrlEncoded
    Call<Passenger> postUserLogin(@Field("username") String username,
                                  @Field("password") String password,
                                  @Field("type") String type);

    @GET("secretInfo")
    Call<ResponseBody> getAccess(@Header("Authorization") String authToken);
}
