package com.example.pa_ad.models;

public class DataModel {
    public String data_id;
    public String device_id;
    public String mqgas;
    public String mlx;
    public String mqhumo;


    public DataModel(String data_id, String device_id, String mqgas, String mlx, String mqhumo) {
        this.data_id = data_id;
        this.device_id = device_id;
        this.mqgas = mqgas;
        this.mlx = mlx;
        this.mqhumo = mqhumo;
    }

    public String getData_id() {
        return data_id;
    }

    public void setData_id(String data_id) {
        this.data_id = data_id;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getMqgas() {
        return mqgas;
    }

    public void setMqgas(String mqgas) {
        this.mqgas = mqgas;
    }

    public String getMlx() {
        return mlx;
    }

    public void setMlx(String mlx) {
        this.mlx = mlx;
    }

    public String getMqhumo() {
        return mqhumo;
    }

    public void setMqhumo(String mqhumo) {
        this.mqhumo = mqhumo;
    }

}
