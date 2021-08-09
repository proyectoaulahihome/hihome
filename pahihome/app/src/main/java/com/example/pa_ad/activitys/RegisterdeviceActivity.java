package com.example.pa_ad.activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.pa_ad.R;
import com.example.pa_ad.RetrofitClient;
import com.example.pa_ad.RetrofitClientPython;
import com.example.pa_ad.models.ReconocimientoModel;
import com.example.pa_ad.models.RegistrationModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

import com.android.volley.Response;

public class RegisterdeviceActivity extends AppCompatActivity {
    private Bundle bundle;
    private EditText namedivice;
    private Button btnsingup;
    private ProgressDialog proDialog;
    private String URL = "https://bsmarthome.herokuapp.com/";
    private RequestQueue requestQueue;
    // variables para mantener sesion
    private SharedPreferences preferences;
    private String user_id, name, last_name, email, address, type, imguser, macdevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_activity);

        init();
        sessionuser();

        btnsingup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(!macdevice.equals("") && !namedivice.getText().toString().equals("")){

                        String registerjson = "{\n" +
                                "   \"firstnameregister\":\""+bundle.getString("firstnameregister")+"\",\n" +
                                "   \"lastnameregister\":\""+bundle.getString("lastnameregister")+"\",\n" +
                                "   \"emailregister\":\""+bundle.getString("emailregister")+"\",\n" +
                                "   \"passwordregister\":\""+bundle.getString("passwordregister")+"\",\n" +
                                "   \"addressregister\":\""+bundle.getString("addressregister")+"\",\n" +
                                "   \"type\":\""+"Administrador"+"\",\n" +
                                "   \"imguser\":\""+"https://elclosetlgbt.com/wp-content/uploads/2020/01/WhatsApp-Image-2020-01-13-at-15.42.30.jpeg"+"\",\n" +
                                "   \"namedevice\":\""+namedivice.getText().toString()+"\",\n" +
                                "   \"macdevice\":\""+macdevice+"\"\n" +

                                "}";
                        Log.d("JSONUSER",registerjson);
                         registrationuservolley(registerjson);
                        //   url();
                      //  registrationuservolley(registerjson);
                       // registrationuser();
                    }
            }
        });
    }
    private void init(){
        namedivice =  (EditText) findViewById(R.id.namedivice);
        btnsingup =  (Button) findViewById(R.id.btnsingup);
        macdevice = getmacdevice();
        preferences = getSharedPreferences("Preferences", MODE_PRIVATE);
        bundle = this.getIntent().getExtras();
    }

    public void url() {

        String urls = "http://192.168.1.3:8080/bsmarthome/webresources/users/userregistration";
        RequestQueue requestQueue2 = Volley.newRequestQueue(this);

        JSONObject postData = new JSONObject();
        try {
            postData.put("firstnameregister", bundle.getString("firstnameregister"));
            postData.put("lastnameregister", bundle.getString("lastnameregister"));
            postData.put("emailregister",bundle.getString("emailregister"));
            postData.put("passwordregister",bundle.getString("passwordregister"));
            postData.put("addressregister","Administrador");
            postData.put("imguser","https://elclosetlgbt.com/wp-content/uploads/2020/01/WhatsApp-Image-2020-01-13-at-15.42.30.jpeg");
            postData.put("namedevice",namedivice.getText().toString());
            postData.put("macdevice",macdevice);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urls, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("response",response.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue2.add(jsonObjectRequest);
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
            return stringMac.substring(0,stringMac.length()-1);

        } catch (SocketException e) {
            e.printStackTrace();
        }
        return stringMac.substring(0,stringMac.length()-1);
    }

    private void gologin() {
        Intent i = new Intent(this, MainActivity.class);
        // bandera para que no se creen nuevas actividades innecesarias
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
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

    private void registrationuservolley(String datajson){

        //Obtención de datos del web service utilzando Volley
        requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(
                Request.Method.POST,"http://192.168.1.3:8080/bsmarthome/webresources/users/userregistration",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int size = response.length();
                        JSONObject json_transform = null;
                        try {
                            json_transform = new JSONObject(response);
                            Log.d("response",response);

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
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
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

}
