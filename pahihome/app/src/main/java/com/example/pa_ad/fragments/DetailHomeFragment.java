package com.example.pa_ad.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.pa_ad.R;
import com.example.pa_ad.models.UserModel;

public class DetailHomeFragment extends Fragment {
    TextView home_detail_name;
    ImageView home_detail_img;
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
        if(objecthome!=null){
            userModel = (UserModel) objecthome.getSerializable("object");
            //Establecer los datos en las vistas
            home_detail_name.setText(userModel.getName());
            Glide.with(getContext()).load(userModel.getImguser()).into(home_detail_img);
        }
    return view;
    }


}
