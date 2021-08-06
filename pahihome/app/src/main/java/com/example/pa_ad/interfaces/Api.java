package com.example.pa_ad.interfaces;

import com.example.pa_ad.models.FetchUserResponse;
import com.example.pa_ad.models.UserModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {

    @GET("users")
    public Call<List<UserModel>> fetchusers();
 //   public Call<FetchUserResponse> fetchusers();




}
