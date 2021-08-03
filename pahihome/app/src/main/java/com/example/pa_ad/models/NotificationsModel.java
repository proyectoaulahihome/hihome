package com.example.pa_ad.models;

import java.io.Serializable;

public class NotificationsModel implements Serializable {
    public int id;
    public int imagen;
    public String nombre;
    public String Des;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDes() {
        return Des;
    }

    public void setDes(String des) {
        Des = des;
    }

    public NotificationsModel(int id, int imagen, String nombre, String des) {
        this.id = id;
        this.imagen = imagen;
        this.nombre = nombre;
        Des = des;
    }
}
