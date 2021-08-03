package com.example.pa_ad.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

 String URL = "https://bsmarthome.herokuapp.com/";
 RequestQueue requestQueue;
 EditText edittextuser;
 EditText edittexpassword;
 Button btnLogin;
 Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
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
                POSTVolley(loginjson);
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

    private void POSTVolley(String datajson){

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
                                    Intent intent = new Intent(MainActivity.this, principal_activity_main.class);
                                    Bundle b = new Bundle();
                                    b.putString("name",json_transform.getJSONObject("data").getString("name"));
                                    b.putString("last_name",json_transform.getJSONObject("data").getString("last_name"));
                                    b.putString("type",json_transform.getJSONObject("data").getString("type"));
                                    b.putString("imguser",json_transform.getJSONObject("data").getString("imguser"));
                                    intent.putExtras(b);
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

}