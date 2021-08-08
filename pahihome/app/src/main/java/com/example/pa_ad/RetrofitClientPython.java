package com.example.pa_ad;

import com.example.pa_ad.interfaces.Api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientPython {
    private static String BASE_URL = "http://127.0.0.1:5000/";
    private static RetrofitClientPython retrofitClient;
    private static Retrofit retrofit;

    private RetrofitClientPython(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized RetrofitClientPython getInstance(){
        if(retrofitClient==null){
            retrofitClient = new RetrofitClientPython();
        }
        return retrofitClient;
    }

    public Api getApi(){
        return  retrofit.create(Api.class);
    }
}
