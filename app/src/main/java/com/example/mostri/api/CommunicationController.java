package com.example.mostri.api;


import android.util.Log;

import androidx.annotation.NonNull;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.mostri.model.ObjectActivationResponse;
import com.example.mostri.model.ObjectDetailsResponse;
import com.example.mostri.model.RankingResponse;
import com.example.mostri.model.SessionResponse;
import com.example.mostri.model.ObjectsResponse;
import com.example.mostri.model.UserDetailsResponse;
import com.example.mostri.model.UserUpdate;
import com.example.mostri.model.UsersShareResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;

public class CommunicationController {
    private static final String BASE_URL = "https://develop.ewlab.di.unimi.it/mc/mostri/";
    private static ApiInterface apiInterface = null;

    @NonNull
    public static ApiInterface getApiInterface() {
        if (apiInterface == null) {
            Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
            apiInterface = retrofit.create(ApiInterface.class);
        }
        return apiInterface;
    }

    public static void createUser(OnUserCreatedListener onUserCreatedListener, OnFailureListener onFailureListener) {
        ApiInterface apiInterface = getApiInterface();
        Call<SessionResponse> createUserCall = apiInterface.createUser();
        createUserCall.enqueue(new Callback<SessionResponse>() {
            @Override
            public void onResponse(@NonNull Call<SessionResponse> call, @NonNull Response<SessionResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    onUserCreatedListener.onUserCreated(response.body().getSessionId(), response.body().getUserId());
                } else {
                    onFailureListener.onFailure(new Exception("Failed to create user"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<SessionResponse> call, @NonNull Throwable t) {
                onFailureListener.onFailure(t);
            }
        });
    }

    public static void getObjects(String sid, String lat, String lon, OnObjectsReceivedListener onObjectsReceivedListener, OnFailureListener onFailureListener) {
        ApiInterface apiInterface = getApiInterface();
        Call<List<ObjectsResponse>> getObjectsCall = apiInterface.getObjects(sid, lat, lon);
        getObjectsCall.enqueue(new Callback<List<ObjectsResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<ObjectsResponse>> call, @NonNull Response<List<ObjectsResponse>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    onObjectsReceivedListener.onObjectsReceived(response.body());
                } else {
                    onFailureListener.onFailure(new Exception("Failed to get objects"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ObjectsResponse>> call, @NonNull Throwable t) {
                onFailureListener.onFailure(t);
            }
        });
    }

    public static void getObjectById(String id, String sid, OnObjectDetailsReceivedListener onObjectDetailsReceivedListener, OnFailureListener onFailureListener) {
        ApiInterface apiInterface = getApiInterface();
        Call<ObjectDetailsResponse> getObjectByIdCall = apiInterface.getObjectById(id, sid);
        getObjectByIdCall.enqueue(new Callback<ObjectDetailsResponse>() {
            @Override
            public void onResponse(@NonNull Call<ObjectDetailsResponse> call, @NonNull Response<ObjectDetailsResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    onObjectDetailsReceivedListener.onObjectDetailsReceived(response.body());
                } else {
                    onFailureListener.onFailure(new Exception("Failed to get object by ID"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ObjectDetailsResponse> call, @NonNull Throwable t) {
                onFailureListener.onFailure(t);
            }
        });
    }

    public static void activateObject(String id, String sid, OnObjectActivationListener onObjectActivationListener, OnFailureListener onFailureListener) {
        ApiInterface apiInterface = getApiInterface();
        Call<ObjectActivationResponse> activateObjectCall = apiInterface.activateObject(id, new ActivationRequest(sid));
        activateObjectCall.enqueue(new Callback<ObjectActivationResponse>() {
            @Override
            public void onResponse(@NonNull Call<ObjectActivationResponse> call, @NonNull Response<ObjectActivationResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    onObjectActivationListener.onObjectActivation(response.body());
                } else {
                    onFailureListener.onFailure(new Exception("Failed to activate object"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ObjectActivationResponse> call, @NonNull Throwable t) {
                onFailureListener.onFailure(t);
            }
        });
    }

    public static void getSharedUsers(String sid, String lat, String lon, OnSharedUsersReceivedListener onSharedUsersReceivedListener, OnFailureListener onFailureListener) {
        ApiInterface apiInterface = getApiInterface();
        Call<List<UsersShareResponse>> getSharedUsersCall = apiInterface.getSharedUsers(sid, lat, lon);
        getSharedUsersCall.enqueue(new Callback<List<UsersShareResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<UsersShareResponse>> call, @NonNull Response<List<UsersShareResponse>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    onSharedUsersReceivedListener.onSharedUsersReceived(response.body());
                } else {
                    onFailureListener.onFailure(new Exception("Failed to get shared users"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<UsersShareResponse>> call, @NonNull Throwable t) {
                onFailureListener.onFailure(t);
            }
        });
    }

    public static void getUserById(String id, String sid, OnUserDetailsReceivedListener onUserDetailsReceivedListener, OnFailureListener onFailureListener) {
        ApiInterface apiInterface = getApiInterface();
        Call<UserDetailsResponse> getUserByIdCall = apiInterface.getUserById(id, sid);
        getUserByIdCall.enqueue(new Callback<UserDetailsResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserDetailsResponse> call, @NonNull Response<UserDetailsResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    onUserDetailsReceivedListener.onUserDetailsReceived(response.body());
                } else {
                    onFailureListener.onFailure(new Exception("Failed to get user by ID"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserDetailsResponse> call, @NonNull Throwable t) {
                onFailureListener.onFailure(t);
            }
        });
    }

    public static void getRanking(String sid, OnRankingReceivedListener onRankingReceivedListener, OnFailureListener onFailureListener) {
        ApiInterface apiInterface = getApiInterface();
        Call<List<RankingResponse>> getRankingCall = apiInterface.getRanking(sid);
        getRankingCall.enqueue(new Callback<List<RankingResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<RankingResponse>> call, @NonNull Response<List<RankingResponse>> response) {
                if (response.isSuccessful()) {
                    //Log.d("TAG", "Lista: " + response.body().size() + ", uid primo utente: " + response.body().get(0).getUid());
                    assert response.body() != null;
                    onRankingReceivedListener.onRankingReceived(response.body());
                } else {
                    onFailureListener.onFailure(new Exception("Failed to get ranking"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<RankingResponse>> call, @NonNull Throwable t) {
                onFailureListener.onFailure(t);
            }
        });
    }

    public static void updateUser(String id, UpdateUser updateUser, OnUserUpdateListener onUserUpdateListener, OnFailureListener onFailureListener) {
        ApiInterface apiInterface = getApiInterface();
        Call<UserUpdate> updateUserCall = apiInterface.updateUser(id, updateUser);
        updateUserCall.enqueue(new Callback<UserUpdate>() {
            @Override
            public void onResponse(@NonNull Call<UserUpdate> call, @NonNull Response<UserUpdate> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    onUserUpdateListener.onUserUpdate(response.body());
                } else {
                    onFailureListener.onFailure(new Exception("Failed to update user"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserUpdate> call, @NonNull Throwable t) {
                onFailureListener.onFailure(t);
            }
        });
    }
}