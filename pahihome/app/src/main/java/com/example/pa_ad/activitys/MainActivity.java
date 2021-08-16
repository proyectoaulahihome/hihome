package com.example.pa_ad.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.pa_ad.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

private String URL = "https://bsmarthome.herokuapp.com/";
private RequestQueue requestQueue;
private EditText edittextuser,edittexpassword;
private Button btnLogin, btnRegister;

// variables para mantener sesion
private SharedPreferences preferences;
private String user_id, name, last_name, email, address, type, imguser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        init();
        if(validatesesion()){
            Toast.makeText(MainActivity.this, "Active session", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(MainActivity.this, "Session not active", Toast.LENGTH_LONG).show();
        }


        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                String loginjson = "{\n" +
                        "   \"email\":\""+edittextuser.getText()+"\",\n" +
                        "   \"password\":\""+edittexpassword.getText()+"\"\n" +
                        "}";
                Log.d("JSONUSER",loginjson);
                Sesion(loginjson);
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                Bundle b = new Bundle();
                startActivity(intent);
            }
        });
    }
    private void Sesion(String datajson){

        //Obtención de datos del web service utilzando Volley
        requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(
                Request.Method.POST,URL+"webresources/users/logIn",
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
                                if(json_transform.getString("flag").equals("true")){
                                    Log.d("response",response);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("user_id",json_transform.getJSONObject("data").getString("user_id"));
                                    editor.putString("name",json_transform.getJSONObject("data").getString("name"));
                                    editor.putString("last_name",json_transform.getJSONObject("data").getString("last_name"));
                                    editor.putString("email",json_transform.getJSONObject("data").getString("email"));
                                    editor.putString("address",json_transform.getJSONObject("data").getString("address"));
                                    editor.putString("type",json_transform.getJSONObject("data").getString("type"));
                                    editor.putString("imguser",json_transform.getJSONObject("data").getString("imguser"));
                                    editor.commit();
                                    goMenu();
                                }
                                else{
                                    Toast.makeText(MainActivity.this, "Incorrect credentials", Toast.LENGTH_LONG).show();
                                    Log.d("response",response);
                                }
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

    private void init(){
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        edittextuser =  (EditText) findViewById(R.id.edittextuser);
        edittexpassword =  (EditText)findViewById(R.id.edittexpassword);
        preferences = getSharedPreferences("Preferences", MODE_PRIVATE);
    }
    private boolean validatesesion(){
        sessionuser();
        if (user_id != null && email != null) {
            Log.d("iduser", user_id);
            Log.d("email", email);
            goMenu();
            return true;
        }else{
            return false;
        }
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

    private void goMenu(){
        Intent i = new Intent(this, processActivity.class);
        // bandera para que no se creen nuevas actividades innecesarias
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

}