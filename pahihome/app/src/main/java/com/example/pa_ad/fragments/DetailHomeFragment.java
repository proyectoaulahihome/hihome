package com.example.pa_ad.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.File;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.pa_ad.R;
import com.example.pa_ad.RetrofitClient;
import com.example.pa_ad.RetrofitClientPython;
import com.example.pa_ad.adapters.ListAdapterHome;
import com.example.pa_ad.models.ReconocimientoModel;
import com.example.pa_ad.models.UserModel;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailHomeFragment extends Fragment {
    private TextView home_detail_name;
    private ImageView home_detail_img;
    private String directorioRaiz;
    private ProgressDialog proDialog;
    private List<String> nombresArchivos;
    private List<String> rutasArchivos;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    View view =inflater.inflate(R.layout.home_detail_fragment, container, false);


        home_detail_name = view.findViewById(R.id.home_detail_name);
        home_detail_img = view.findViewById(R.id.home_detail_img);
        //Crear objeto bundler para recibir el objeto enviado por argumentos
        Bundle objecthome = getArguments();
        UserModel userModel = null;
        //validacion para verificar si existen argumentos enviados para mostrar
        if(objecthome!=null) {

            userModel = (UserModel) objecthome.getSerializable("object");
            String macdevice = getmacdevice();
            home_detail_name.setText(macdevice.substring(0,macdevice.length()-1));
            //Establecer los datos en las vistas
           //  home_detail_name.setText(userModel.getName());
            Glide.with(getContext()).load(userModel.getImguser()).into(home_detail_img);
        }
    return view;
    }


    private void VerDirectorio(String rutaDirectorio){
        nombresArchivos = new ArrayList<String>();
        rutasArchivos = new ArrayList<String>();
        int count = 0;
        File directorioActual = new File(rutaDirectorio);
        File[] listaArchivos = directorioActual.listFiles();

        if (!rutaDirectorio.equals(directorioRaiz)){
            nombresArchivos.add("../");
            rutasArchivos.add(directorioActual.getParent());
            count = 1;
        }

        for (File archivo : listaArchivos){
            rutasArchivos.add(archivo.getPath());
        }
        Collections.sort(rutasArchivos, String.CASE_INSENSITIVE_ORDER);

        for(int i = count; i < rutasArchivos.size(); i++){
            File archivo = new File(rutasArchivos.get(i));
            if(archivo.isFile()){
                nombresArchivos.add(archivo.getName());
            }else{
                nombresArchivos.add("/"+archivo.getName());
            }
        }
        if(listaArchivos.length < 1){
            nombresArchivos.add("No hay ningun archivo");
            rutasArchivos.add(rutaDirectorio);
        }

        // Declaramos el Iterador e imprimimos los Elementos del ArrayList
        Iterator<String> nombreIterator = rutasArchivos.iterator();
        while(nombreIterator.hasNext()){
            String elemento = nombreIterator.next();
            System.out.print(elemento+" / ");
        }


    }
    public void PostData(){

    }
    private String getmacdevice(){
        String stringMac = "";
        try {
            List<NetworkInterface> networkInterfaceslist = Collections.list(NetworkInterface.getNetworkInterfaces());

            for(NetworkInterface networkInterface:networkInterfaceslist){
                if(networkInterface.getName().equalsIgnoreCase("wlan0")){
                    for (int i=0; i<networkInterface.getHardwareAddress().length;i++){

                        String stringMacByte = Integer.toHexString(networkInterface.getHardwareAddress()[i] & 0xFF);

                        if(stringMacByte.length() == 1){
                            stringMacByte = "0" + stringMac;
                        }

                        stringMac = stringMac + stringMacByte.toUpperCase()+":";
                    }
                    break;
                }
            }
            return stringMac;

        } catch (SocketException e) {
            e.printStackTrace();
        }
        return stringMac;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ReconocimientoModel reconocimientoModel = new ReconocimientoModel("Imagenes y Videos de Prueba/Michael.mp4","hola");
        Call<ReconocimientoModel> call = RetrofitClientPython.getInstance().getApi().PostDataReconocimiento(reconocimientoModel);
        call.enqueue(new Callback<ReconocimientoModel>() {
            @Override
            public void onResponse(Call<ReconocimientoModel> call, Response<ReconocimientoModel> response) {
                proDialog.dismiss();
                Log.d("JSONRECONOCIMIENTO","HOLA");
            }

            @Override
            public void onFailure(Call<ReconocimientoModel> call, Throwable t) {
                Log.d("JSONRECONOCIMIENTO","ERROR");
            }
        });

    }


}
