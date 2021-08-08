package com.example.pa_ad.models;

import java.io.Serializable;

public class ReconocimientoModel  implements Serializable {

    private String reco_user_ruta;
    private String reco_user_name;

    public ReconocimientoModel(String reco_user_ruta, String reco_user_name) {
        this.reco_user_ruta = reco_user_ruta;
        this.reco_user_name = reco_user_name;
    }

    public String getReco_user_ruta() {
        return reco_user_ruta;
    }

    public void setReco_user_ruta(String reco_user_ruta) {
        this.reco_user_ruta = reco_user_ruta;
    }

    public String getReco_user_name() {
        return reco_user_name;
    }

    public void setReco_user_name(String reco_user_name) {
        this.reco_user_name = reco_user_name;
    }
}
