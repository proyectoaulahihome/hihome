package com.example.pa_ad.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FetchUserResponse {
    @SerializedName("users")
    List<UserModel> userList;

    public FetchUserResponse(List<UserModel> userList) {
        this.userList = userList;
    }

    public List<UserModel> getUserList() {
        return userList;
    }

    public void setUserList(List<UserModel> userList) {
        this.userList = userList;
    }
}
