package com.example.pa_ad.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.pa_ad.R;
import com.example.pa_ad.RetrofitClient;
import com.example.pa_ad.RetrofitClientPython;
import com.example.pa_ad.activitys.MainActivity;
import com.example.pa_ad.adapters.ListAdapterHome;
import com.example.pa_ad.models.ReconocimientoModel;
import com.example.pa_ad.models.UserModel;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailHomeFragment extends Fragment {
    private TextView home_detail_name, txtmqgasuser, txtmlxuser, txtmqhumouser;

    private ImageView home_detail_img;
    private String URL = "https://bsmarthome.herokuapp.com/webresources/";
    private RequestQueue requestQueue;
    BarChart barchart;
    private Handler handler;
    private Runnable mTicker;

    String []labels=new String[] {"MQGAS","MLX"};
    int []airqdata=new int[]{108,21,71,70};

    // variables para mantener sesion
    private SharedPreferences preferences;
    private String user_id, name, last_name, email, address, type, imguser;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    View view =inflater.inflate(R.layout.home_detail_fragment, container, false);
    barchart = (BarChart)view.findViewById(R.id.barchartuser);
    return view;

    }


    private void exec(String UserReg){
        handler= new Handler();
        mTicker = new Runnable() {
            @Override
            public void run() {
                grafi(UserReg);
                handler.postDelayed(this,5000);//se ejecutara cada 1 segundos
            }
        };
        handler.postDelayed(mTicker,5000);//se ejecutara cada 5 segundos
    }

    public void PostData(String datajson){
        //Obtención de datos del web service utilzando Volley
        requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest request = new StringRequest(
                Request.Method.POST,URL+"webresources/",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int size = response.length();
                        response = fixEncoding(response);
                        JSONObject json_transform = null;
                        try {
                            if (size > 0)
                            {
                                json_transform = new JSONObject(response);
                                Log.d("Respuesta", response);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", String.valueOf(error));
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=utf-8");
                params.put("Accept", "application/json");
                return params;
            }
            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return datajson == null ? "{}".getBytes("utf-8") : datajson.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {

                    return null;
                }
            }
        };
        requestQueue.add(request);
    }


    private void grafi(String UserReg){

        if(user_id != null && email != null){
            Description descrio=new Description();
            barchart.setDescription(descrio);

           // requestQueue = Volley.newRequestQueue(getActivity());

            StringRequest request = new StringRequest(
                    Request.Method.GET,
                    URL +"data/shearchforuser",
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            int size = response.length();
                            boolean band = false;
                            response = fixEncoding(response);
                          //  Log.d("Respuesta", response);
                            try {
                                ArrayList<BarEntry> barEntries=new ArrayList<>();
                                JSONObject json_transform = null;

                                JSONArray jsonarray = new JSONArray(response);

                                for(int i=0; i < jsonarray.length(); i++) {
                                    json_transform = jsonarray.getJSONObject(i);
                                    if((json_transform.getString("u_detected")).equals(UserReg)){
                                        txtmqgasuser.setText(json_transform.getString("mqgas"));
                                        txtmlxuser.setText(json_transform.getString("mlx"));
                                        barEntries.add(new BarEntry( 0, json_transform.getInt("mqgas")));
                                        barEntries.add(new BarEntry( 1, json_transform.getInt("mlx")));
                                        band = true;
                                    }else{

                                    }
                                    if(band){
                                        BarDataSet barDataSet = new BarDataSet(barEntries, "Gases");
                                        barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
                                        //        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                                        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                                        barDataSet.setHighlightEnabled(true);
                                        barDataSet.setHighLightColor(Color.RED);
                                        barDataSet.setValueTextSize(getDefaultViewModelProviderFactory().hashCode());
                                        barDataSet.setValueTextColor(123);

                                        BarData barData = new BarData(barDataSet);

                                        barchart.getDescription().setText("No. of real-time gas concentrations");
                                        barchart.getDescription().setTextSize(12);
                                        barchart.setDrawMarkers(true);
                                        barchart.setMarker(barchart.getMarkerView());
                                        barchart.getAxisLeft().setAxisMinimum(0);
                                        barchart.getXAxis().setPosition(XAxis.XAxisPosition.BOTH_SIDED);

                                        ArrayList<String> labels = new ArrayList<String> ();

                                        labels.add( "MQGAS");
                                        labels.add( "MLX");

                                        barchart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
                                        barchart.animateY(1000);

                                        barchart.getXAxis().setGranularityEnabled(true);
                                        barchart.getXAxis().setGranularity(1.0f);
                                        barchart.getXAxis().setLabelCount(barDataSet.getEntryCount());

                                        barchart.setData(barData);
                                    }


                                    //    Log.d("mqgas",String.valueOf(json_transform.getInt("mqgas")));

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/json; charset=utf-8");
                    params.put("Accept", "application/json");
                    return params;
                }
            };
            if (requestQueue == null) {
                requestQueue = Volley.newRequestQueue(getActivity());
                requestQueue.add(request);
            } else {
                requestQueue.add(request);
            }
            // requestQueue.add(request);

        }else{
            Toast.makeText(getActivity(), "No session", Toast.LENGTH_LONG).show();
            gologin();
        }


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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        onDestroy ();
    }

    @Override public void onDestroy ()
    {
        handler.removeCallbacks(mTicker);
        super.onDestroy ();
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
        init();
        sessionuser();
        home_detail_name = (TextView) view.findViewById(R.id.home_detail_name);
        home_detail_img = (ImageView) view.findViewById(R.id.home_detail_img);
        txtmqgasuser = (TextView) view.findViewById(R.id.txtmqgasuser);
        txtmlxuser = (TextView) view.findViewById(R.id.txtmlxuser);
        //Crear objeto bundler para recibir el objeto enviado por argumentos
        Bundle objecthome = getArguments();
        UserModel userModel = null;
        //validacion para verificar si existen argumentos enviados para mostrar
        if(objecthome!=null) {
            userModel = (UserModel) objecthome.getSerializable("object");
            String macdevice = getmacdevice();
           // home_detail_name.setText(macdevice.substring(0,macdevice.length()-1));
            //Establecer los datos en las vistas
            //  home_detail_name.setText(userModel.getName());


            String UserReg = userModel.getName() + "_" + userModel.getLast_name();
            Log.d("USUARIO A BUSCAR",UserReg);
            home_detail_name.setText(userModel.getName() + " " + userModel.getLast_name());
            Glide.with(getContext()).load(userModel.getImguser()).into(home_detail_img);
            String jsondatauser = "{\n" +
                    "   \"UserReg\":\""+UserReg+"\",\n" +
                    "}";
            Log.d("JSONUSER",jsondatauser);

            exec(UserReg);
        }

    }

    private void gologin() {
        Intent i = new Intent(getActivity(), MainActivity.class);
        // bandera para que no se creen nuevas actividades innecesarias
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
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


}
