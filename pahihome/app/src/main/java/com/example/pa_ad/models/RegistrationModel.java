package com.example.pa_ad.models;

public class RegistrationModel {

    private String name;
    private String last_name;
    private String email;
    private String password;
    private String address;
    private String type;
    private String imguser;
    private String namdevice;
    private String mac;

    public RegistrationModel(String name, String last_name, String email, String password, String address, String type, String imguser, String namdevice, String mac) {
        this.name = name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.type = type;
        this.imguser = imguser;
        this.namdevice = namdevice;
        this.mac = mac;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImguser() {
        return imguser;
    }

    public void setImguser(String imguser) {
        this.imguser = imguser;
    }

    public String getNamdevice() {
        return namdevice;
    }

    public void setNamdevice(String namdevice) {
        this.namdevice = namdevice;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }
}
