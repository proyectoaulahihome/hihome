package com.example.pa_ad.interfaces;

import com.example.pa_ad.models.FetchUserResponse;
import com.example.pa_ad.models.ReconocimientoModel;
import com.example.pa_ad.models.RegistrationModel;
import com.example.pa_ad.models.UserModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {

    @GET("users")
    public Call<List<UserModel>> fetchusers();
 //   public Call<FetchUserResponse> fetchusers();

    @POST("reconocimiento")
    Call<ReconocimientoModel> PostDataReconocimiento(@Body ReconocimientoModel reconocimientoModel);

    @POST("Registration")
    Call<RegistrationModel> PostDataRegistrationuser(@Body RegistrationModel registrationModel);

}
