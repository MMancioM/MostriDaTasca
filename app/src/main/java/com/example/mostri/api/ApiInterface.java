package com.example.mostri.api;

import com.example.mostri.model.ObjectActivationResponse;
import com.example.mostri.model.ObjectDetailsResponse;
import com.example.mostri.model.RankingResponse;
import com.example.mostri.model.SessionResponse;
import com.example.mostri.model.ObjectsResponse;
import com.example.mostri.model.UserDetailsResponse;
import com.example.mostri.model.UserUpdate;
import com.example.mostri.model.UsersShareResponse;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.*;

public interface ApiInterface {

        @POST("users")
        Call<SessionResponse> createUser();

        @GET("users")
        Call<List<UsersShareResponse>> getSharedUsers(@Query("sid") String sid, @Query("lat") String lat, @Query("lon") String lon);

        @GET("users/{id}")
        Call<UserDetailsResponse> getUserById(@Path("id") String id, @Query("sid") String sid);

        @PATCH("users/{id}")
        Call<UserUpdate> updateUser(@Path("id") String id, @Body UpdateUser userUpdate);

        @GET("objects")
        Call<List<ObjectsResponse>> getObjects(@Query("sid") String sid, @Query("lat") String lat, @Query("lon") String lon);

        @GET("objects/{id}")
        Call<ObjectDetailsResponse> getObjectById(@Path("id") String id, @Query("sid") String sid);

        @POST("objects/{id}/activate")
        Call<ObjectActivationResponse> activateObject(@Path("id") String id, @Body ActivationRequest request);

        @GET("ranking")
        Call<List<RankingResponse>> getRanking(@Query("sid") String sid);
}
