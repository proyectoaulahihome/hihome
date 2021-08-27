package com.example.pa_ad.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.pa_ad.R;
import com.example.pa_ad.RetrofitClient;
import com.example.pa_ad.RetrofitClientPython;
import com.example.pa_ad.activitys.MainActivity;
import com.example.pa_ad.models.DataModel;
import com.example.pa_ad.models.NotificationsModel;
import com.example.pa_ad.models.ReconocimientoModel;
import com.example.pa_ad.models.UserModel;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RealTimeControlFragment extends Fragment {

    private RequestQueue requestQueue;
    //private String URL = "https://bsmarthome.herokuapp.com/webresources/";
    private String URL = "http://aplicaciones.uteq.edu.ec/bsmarthome/webresources/";
    private Handler handler;
    private Runnable mTicker;
    PieChart piechart;
    TextView txtmqgas, txtmlx, txtmqhumo;
    private ProgressDialog proDialog;
    List<DataModel> dataModel;
    // variables para mantener sesion
    private SharedPreferences preferences;
    private String user_id, name, last_name, email, address, type, imguser, device_id;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.realtimecontrol_fragment,container,false);
        piechart = (PieChart)view.findViewById(R.id.Pastelchart);
        txtmqgas =  (TextView) view.findViewById(R.id.txtmqgas);
        txtmlx =  (TextView) view.findViewById(R.id.txtmlx);
        init();
        sessionuser();
        exec();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        onDestroy ();
    }

    private void exec(){
        handler= new Handler();
        mTicker = new Runnable() {
            @Override
            public void run() {
                grafi();
                handler.postDelayed(this,5000);//se ejecutara cada 1 segundos
            }
        };
        handler.postDelayed(mTicker,5000);//se ejecutara cada 5 segundos
    /*    handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                grafi();//llamamos nuestro metodo
                handler.postDelayed(this,1000);//se ejecutara cada 10 segundos
            }
        },5000);//empezara a ejecutarse después de 5 milisegundos */
    }
    @Override public void onDestroy ()
    {
        handler.removeCallbacks(mTicker);
        super.onDestroy ();
    }

    private void grafi(){

        if(user_id != null && email != null){
            Description descrio=new Description();
            //descrio.setText("Actividad");
            piechart.setDescription(descrio);

            String datajson = "{\n" +
                    "   \"device_id\":\""+device_id+"\"\n" +
                    "}";
           // Log.d("JSONUSER",datajson);

            //  requestQueue = Volley.newRequestQueue(getActivity());
            StringRequest request = new StringRequest(
                    Request.Method.POST,
                    URL +"data/shearchbyruserdevice",
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            int size = response.length();
                            response = fixEncoding(response);
                            JSONObject json_transform = null;
                            ArrayList<PieEntry> pieEntries=new ArrayList<>();
                            try {
                                if (size > 0)
                                {
                                    json_transform = new JSONObject(response);
                                    if(json_transform.getString("flag").equals("true")){
                                        JSONArray jsonArray = json_transform.getJSONArray("data");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject object = jsonArray.getJSONObject(i);
                                            //  Log.d("name",object.get("user_id").toString());
                                            txtmqgas.setText(object.get("mqgas").toString());
                                            txtmlx.setText(object.get("mlx").toString());
                                            pieEntries.add(new PieEntry(object.getInt("mqgas"), "mqgas"));
                                            pieEntries.add(new PieEntry(object.getInt("mlx"), "mlx"));
                                        }
                                        piechart.animateX(2500, Easing.EasingOption.EaseOutCirc);
                                        PieDataSet pieDataSet=new PieDataSet(pieEntries, "MQGAS" +"-"+ "MLX");
                                        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                                        PieData pieData=new PieData(pieDataSet);

                                        piechart.setData(pieData);
                                    }else{
                                        Toast.makeText(getActivity(), "No data", Toast.LENGTH_LONG).show();
                                        Log.d("response",response);
                                    }
                                }

                            } catch (Exception e) {
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
                    Map<String, String> params = new HashMap<String, String>();
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
            if (requestQueue == null) {
                requestQueue = Volley.newRequestQueue(getActivity());
                requestQueue.add(request);
            } else {
                requestQueue.add(request);
            }

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
        device_id= preferences.getString("device_id",null);
    }

    private void gologin() {
        Intent i = new Intent(getActivity(), MainActivity.class);
        // bandera para que no se creen nuevas actividades innecesarias
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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