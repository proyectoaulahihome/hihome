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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.pa_ad.R;
import com.example.pa_ad.RetrofitClient;
import com.example.pa_ad.RetrofitClientPython;
import com.example.pa_ad.models.ReconocimientoModel;
import com.example.pa_ad.models.RegistrationModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterdeviceActivity extends AppCompatActivity {
    private Bundle bundle;
    private EditText macdevice, namedivice;
    private Button btnsingup;
    private ProgressDialog proDialog;
    private String URL = "https://bsmarthome.herokuapp.com/";
    private RequestQueue requestQueue;
    // variables para mantener sesion
    private SharedPreferences preferences;
    private String user_id, name, last_name, email, address, type, imguser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        init();
        sessionuser();

        btnsingup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user_id != null && email != null){
                    if(!macdevice.getText().toString().equals("") && !namedivice.getText().toString().equals("")){
                        proDialog = new ProgressDialog(getApplicationContext());
                        proDialog.setTitle("Registration");
                        proDialog.setMessage("Processing registration please wait...");
                        proDialog.show();
                        String registerjson = "{\n" +
                                "   \"firstnameregister\":\""+bundle.getString("firstnameregister")+"\",\n" +
                                "   \"lastnameregister\":\""+bundle.getString("lastnameregister")+"\"\n" +
                                "   \"emailregister\":\""+bundle.getString("emailregister")+"\"\n" +
                                "   \"passwordregister\":\""+bundle.getString("passwordregister")+"\"\n" +
                                "   \"addressregister\":\""+bundle.getString("addressregister")+"\"\n" +
                                "   \"type\":\""+"Administrador"+"\"\n" +
                                "   \"imguser\":\""+"https://elclosetlgbt.com/wp-content/uploads/2020/01/WhatsApp-Image-2020-01-13-at-15.42.30.jpeg"+"\"\n" +
                                "   \"namedevice\":\""+namedivice.getText().toString()+"\"\n" +
                                "   \"macdevice\":\""+macdevice.getText().toString()+"\"\n" +

                                "}";
                        Log.d("JSONUSER",registerjson);
                      //  registrationuservolley(registerjson);
                       // registrationuser();
                    }
                }else{
                    Toast.makeText(RegisterdeviceActivity.this, "No session", Toast.LENGTH_LONG).show();
                    gologin();
                }
            }
        });


    }
    private void init(){
        macdevice =  (EditText) findViewById(R.id.macdevice);
        namedivice =  (EditText) findViewById(R.id.namedivice);
        btnsingup =  (Button) findViewById(R.id.btnsingup);
        preferences = getSharedPreferences("Preferences", MODE_PRIVATE);
        bundle = this.getIntent().getExtras();
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
                Request.Method.POST,URL+"webresources/users/userregistration",
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

    private void registrationuser(){
        RegistrationModel registrationModel = new RegistrationModel(bundle.getString("firstnameregister"),
                bundle.getString("lastnameregister"),bundle.getString("emailregister"),
                bundle.getString("passwordregister"),bundle.getString("addressregister"),
                "Administrador","https://elclosetlgbt.com/wp-content/uploads/2020/01/WhatsApp-Image-2020-01-13-at-15.42.30.jpeg",
                namedivice.getText().toString(),macdevice.getText().toString());
        Call<RegistrationModel> call = RetrofitClient.getInstance().getApi().PostDataRegistrationuser(registrationModel);
        call.enqueue(new Callback<RegistrationModel>() {
            @Override
            public void onResponse(Call<RegistrationModel> call, Response<RegistrationModel> response) {

            }

            @Override
            public void onFailure(Call<RegistrationModel> call, Throwable t) {

            }
        });
    }

}
