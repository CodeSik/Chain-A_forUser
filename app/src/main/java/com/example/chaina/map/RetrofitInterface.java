package com.example.chaina.map;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RetrofitInterface {
    @Headers({"accept: application/json", "content-type: application/json"})

    @GET("/")
    Call<ArrayList<RecordResponse>> get();

    @POST("/")
    Call<RecordResponse> post( @Body ArrayList<RecordResponse> list );
}
