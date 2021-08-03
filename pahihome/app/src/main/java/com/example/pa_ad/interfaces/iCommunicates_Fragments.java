package com.example.pa_ad.interfaces;


import com.example.pa_ad.models.NotificationsModel;
import com.example.pa_ad.models.UserModel;

public interface iCommunicates_Fragments {
    //esta interface se encarga de realizar la comunicacion entre la lista de personas y el detalle
    public void SendHome(UserModel usermodel); //se transportara un objeto de tipo usermodel
    //(En la clase usermodel se implementa Serializable para poder transportar un objeteo a otro)

    // para la comunicación de la notificacion con el otro fragment
    public void SenNotification(NotificationsModel notificationsModel);
}
