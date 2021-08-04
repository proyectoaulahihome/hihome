package com.example.pa_ad.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class MainActivity extends AppCompatActivity {

private String URL = "https://bsmarthome.herokuapp.com/";
private RequestQueue requestQueue;
private EditText edittextuser;
private EditText edittexpassword;
private Button btnLogin;
private Button btnRegister;

// variables para mantener sesion
private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        init();
        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                edittextuser =  (EditText) findViewById(R.id.edittextuser);
                edittexpassword =  (EditText)findViewById(R.id.edittexpassword);
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
                //DataStatic.gerUrlApi("persona/logIn"),
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int size = response.length();
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
                                    Intent intent = new Intent(MainActivity.this, processActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                                else{
                                    Log.d("Data","Credenciales Incorrectas");
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

    private void init(){
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        preferences = getSharedPreferences("Preferences", MODE_PRIVATE);
    }

}