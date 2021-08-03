package com.example.pa_ad.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.pa_ad.R;
import com.example.pa_ad.adapters.ListAdapterHome;
import com.example.pa_ad.interfaces.iCommunicates_Fragments;
import com.example.pa_ad.models.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    String URL = "https://bsmarthome.herokuapp.com/";
    RequestQueue requestQueue;
    List<UserModel> ListElementsHome;
    RecyclerView recyclerViewHome;

    //referencias para comunicar fragments
    Activity activitys;
    iCommunicates_Fragments interfacecommunicates_Fragments;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_home, container, false);
        ListElementsHome = new ArrayList<>();
        recyclerViewHome= view.findViewById(R.id.ListRecyclerViewHome);
        recyclerViewHome.setLayoutManager(new LinearLayoutManager(getContext()));
       // GetUserVolley();
        cargarLista();
        ListAdapterHome listAdapterHome = new ListAdapterHome(ListElementsHome, getContext());
        recyclerViewHome.setAdapter(listAdapterHome);

        listAdapterHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"Selecciona: " +
                        ListElementsHome.get(recyclerViewHome.getChildAdapterPosition(view)).getName(),
                        Toast.LENGTH_SHORT).show();
                interfacecommunicates_Fragments.SendHome(ListElementsHome.get(recyclerViewHome.getChildAdapterPosition(view)));
            }
        });
        return view;

    }
    public void cargarLista(){
        ListElementsHome.add(new UserModel("1","Martha","Guerrero","martha-guerrero@hotmail.com","8d23cf6c86e834a7aa6eded54c26ce2bb2e74903538c61bdd5d2197997ab2f72","Guayaquil, Urdesa","Usuario","https://elclosetlgbt.com/wp-content/uploads/2020/01/WhatsApp-Image-2020-01-13-at-15.42.30.jpeg"));
        ListElementsHome.add(new UserModel("2","Jorge","Molina","jorge-molina@hotmail.com","a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3","Quevedo, Guayacan","Administrador","https://i.pinimg.com/736x/66/8e/db/668edbd6920ecc00d085d484f6e54c5d.jpg"));
    }

    private void GetUserVolley(){
        boolean variable=false;
        requestQueue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(
                Request.Method.GET,
                URL+"webresources/users",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int size = response.length();
                        Log.d("json",response);
                        Log.d("size",String.valueOf(size));
                        try {
                            if (size > 0)
                            {
                                JSONArray Ja = new JSONArray(response);
                                for(int i=0; i < Ja.length(); i++)
                                {
                                    JSONObject jsonObject = new JSONObject(Ja.get(i).toString());
                                    ListElementsHome.add(new UserModel(
                                            jsonObject.getString("user_id"),
                                            jsonObject.getString("name"),
                                            jsonObject.getString("last_name"),
                                            jsonObject.getString("email"),
                                            jsonObject.getString("password"),
                                            jsonObject.getString("address"),
                                            jsonObject.getString("type"),
                                            jsonObject.getString("imguser")));
                                 /*   Log.d("user_id",jsonObject.getString("user_id"));
                                    Log.d("name",jsonObject.getString("name"));
                                    Log.d("last_name",jsonObject.getString("last_name"));
                                    Log.d("email",jsonObject.getString("email"));
                                    Log.d("password",jsonObject.getString("password"));
                                    Log.d("address",jsonObject.getString("address"));
                                    Log.d("type",jsonObject.getString("type"));
                                    Log.d("imguser",jsonObject.getString("imguser")); */
                                }
                                Log.d("lista",String.valueOf(ListElementsHome.size()));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        requestQueue.add(request);
    }

    public void ShowData(){

    }
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if(context instanceof Activity){
            this.activitys = (Activity) context;
            interfacecommunicates_Fragments = (iCommunicates_Fragments) this.activitys;
        }
    }
    @Override
    public void onDetach(){
        super.onDetach();
    }
}