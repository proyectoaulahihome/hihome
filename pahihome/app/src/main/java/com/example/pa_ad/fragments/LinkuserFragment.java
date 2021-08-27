package com.example.pa_ad.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pa_ad.R;
import com.example.pa_ad.activitys.RegisterActivity;

public class LinkuserFragment extends Fragment {

    private EditText firstnameregister, lastnameregister, emailregister, passwordregister,
            repeatpasswordregister, addressregister;
    private Button btnsavee;
    // variables para mantener sesion
    private SharedPreferences preferences;
    private String user_id, name, last_name, email, address, type, imguser,device_id;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.linkuser_fragment, container, false);
        init();
        sessionuser();

        firstnameregister =  (EditText) view.findViewById(R.id.fistnameregister);
        lastnameregister =  (EditText) view.findViewById(R.id.lastnameregister);
        emailregister =  (EditText) view.findViewById(R.id.emailregister);
        passwordregister =  (EditText) view.findViewById(R.id.passwordregister);
        repeatpasswordregister =  (EditText) view.findViewById(R.id.repeatpasswordregister);
        addressregister  =  (EditText) view.findViewById(R.id.addressregister);
        btnsavee =  (Button) view.findViewById(R.id.btnsavee);


        btnsavee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Uppss!....In development process", Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

    private void init(){
        preferences = this.getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
    }
    public void sessionuser(){
        user_id = preferences.getString("user_id",null);
        name= preferences.getString("name",null);
        last_name= preferences.getString("last_name",null);
        email= preferences.getString("email",null);
        address= preferences.getString("address",null);
        type= preferences.getString("type",null);
        imguser= preferences.getString("imguser",null);
        device_id= preferences.getString("device_id",null);
    }

}
