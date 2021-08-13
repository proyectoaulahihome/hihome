package com.example.pa_ad.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.pa_ad.R;
import com.example.pa_ad.RetrofitClient;
import com.example.pa_ad.activitys.MainActivity;
import com.example.pa_ad.activitys.RegisterActivity;
import com.example.pa_ad.adapters.ListAdapterHome;
import com.example.pa_ad.interfaces.Api;
import com.example.pa_ad.interfaces.iCommunicates_Fragments;
import com.example.pa_ad.models.FetchUserResponse;
import com.example.pa_ad.models.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    private String URL = "https://bsmarthome.herokuapp.com/";
    private RequestQueue requestQueue;
     List<UserModel> ListElementsHome;
    private RecyclerView recyclerViewHome;
    private ListAdapterHome listAdapterHome;
    public String json;
    //referencias para comunicar fragments
    private Activity activitys;
    private boolean dataIsLoaded;
    private iCommunicates_Fragments interfacecommunicates_Fragments;
    private ProgressDialog proDialog;
    // variables para mantener sesion
    private SharedPreferences preferences;
    private String user_id, name, last_name, email, address, type, imguser;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_home, container, false);
        init();
        proDialog = new ProgressDialog(getActivity());
        proDialog.setTitle("Data");
        proDialog.setMessage("Loading data please wait...");
        proDialog.show();
        sessionuser();
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
    }

    private void gologin() {
        Intent i = new Intent(getActivity(), MainActivity.class);
        // bandera para que no se creen nuevas actividades innecesarias
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(user_id != null && email != null){

            recyclerViewHome= view.findViewById(R.id.ListRecyclerViewHome);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerViewHome.setLayoutManager(layoutManager);
            recyclerViewHome.setHasFixedSize(true);

            //  recyclerViewHome.setLayoutManager(new LinearLayoutManager(getActivity()));

            Call<List<UserModel>> call = RetrofitClient.getInstance().getApi().fetchusers();
            call.enqueue(new Callback<List<UserModel>>() {
                @Override
                public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                    proDialog.dismiss();
                    ListElementsHome = response.body();
                    listAdapterHome  = new ListAdapterHome(ListElementsHome, getActivity());
                    recyclerViewHome.setAdapter(listAdapterHome);

                    listAdapterHome.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getActivity(),"Selecciona: " +
                                            ListElementsHome.get(recyclerViewHome.getChildAdapterPosition(view)).getName(),
                                    Toast.LENGTH_SHORT).show();
                            interfacecommunicates_Fragments.SendHome(ListElementsHome.get(recyclerViewHome.getChildAdapterPosition(view)));
                        }
                    });
                }
                @Override
                public void onFailure(Call<List<UserModel>> call, Throwable t) {

                }
            });
        }else{
            Toast.makeText(getActivity(), "No session", Toast.LENGTH_LONG).show();
            gologin();
        }
    }

    public void cargarLista(){
        ListElementsHome.add(new UserModel("1","Martha","Guerrero","martha-guerrero@hotmail.com","8d23cf6c86e834a7aa6eded54c26ce2bb2e74903538c61bdd5d2197997ab2f72","Guayaquil, Urdesa","Usuario","https://elclosetlgbt.com/wp-content/uploads/2020/01/WhatsApp-Image-2020-01-13-at-15.42.30.jpeg"));
        ListElementsHome.add(new UserModel("2","Jorge","Molina","jorge-molina@hotmail.com","a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3","Quevedo, Guayacan","Administrador","https://i.pinimg.com/736x/66/8e/db/668edbd6920ecc00d085d484f6e54c5d.jpg"));
    }

    private void getuser(){
        requestQueue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(
                Request.Method.GET,
                URL+"webresources/users",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int size = response.length();
                        response = fixEncoding(response);
                        if (size > 0){
                            try {
                                JSONArray Ja = new JSONArray(response);
                                for(int i=0; i < Ja.length(); i++)
                                {
                                    JSONObject jsonObject = new JSONObject(Ja.get(i).toString());
                                    Log.d("user_id",jsonObject.getString("user_id"));
                                    Log.d("name",jsonObject.getString("name"));
                                    Log.d("last_name",jsonObject.getString("last_name"));
                                    Log.d("email",jsonObject.getString("email"));
                                    Log.d("password",jsonObject.getString("password"));
                                    Log.d("address",jsonObject.getString("address"));
                                    Log.d("type",jsonObject.getString("type"));
                                    Log.d("imguser",jsonObject.getString("imguser"));
                                    ListElementsHome.add(new UserModel(jsonObject.getString("user_id"),
                                    jsonObject.getString("name"),
                                    jsonObject.getString("last_name"),
                                    jsonObject.getString("email"),
                                    jsonObject.getString("password"),
                                    jsonObject.getString("address"),
                                    jsonObject.getString("type"),
                                    jsonObject.getString("imguser")));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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

    public static String fixEncoding(String response) {
        try {
            byte[] u = response.toString().getBytes(
                    "ISO-8859-1");
            response = new String(u, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        return response;
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