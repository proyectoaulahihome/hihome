package com.example.pa_ad.activitys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.pa_ad.R;
import com.example.pa_ad.fragments.HomeFragment;

public class RegisterActivity extends AppCompatActivity {

    private EditText firstnameregister, lastnameregister, emailregister, passwordregister,
            repeatpasswordregister, addressregister;
    private Button btnnextdevice;
    // variables para mantener sesion
    private SharedPreferences preferences;
    private String user_id, name, last_name, email, address, type, imguser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        init();
        sessionuser();

        btnnextdevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!firstnameregister.getText().toString().equals("") && !lastnameregister.getText().toString().equals("")
                        && !emailregister.getText().toString().equals("") && !passwordregister.getText().toString().equals("")
                        && !repeatpasswordregister.getText().toString().equals("") && addressregister.getText().toString().equals("")) {
                    if(passwordregister.getText().toString().equals(repeatpasswordregister.getText().toString())){
                        goRegisterDevice();
                    }
                    else{
                        Toast.makeText(RegisterActivity.this,
                                "Confirmation of incorrect password", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(RegisterActivity.this,
                            "Empty fields", Toast.LENGTH_LONG).show();
                }
            }
        });



    }
    private void init(){

        firstnameregister =  (EditText) findViewById(R.id.fistnameregister);
        lastnameregister =  (EditText) findViewById(R.id.lastnameregister);
        emailregister =  (EditText) findViewById(R.id.emailregister);
        passwordregister =  (EditText) findViewById(R.id.passwordregister);
        repeatpasswordregister =  (EditText) findViewById(R.id.repeatpasswordregister);
        addressregister  =  (EditText) findViewById(R.id.addressregister);
        btnnextdevice =  (Button) findViewById(R.id.btnnextdevice);

        preferences = getSharedPreferences("Preferences", MODE_PRIVATE);
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

    private void goRegisterDevice(){
        Intent i = new Intent(this, RegisterdeviceActivity.class);
        // bandera para que no se creen nuevas actividades innecesarias
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle b = new Bundle();
        b.putString("firstnameregister", firstnameregister.getText().toString());
        b.putString("lastnameregister", lastnameregister.getText().toString());
        b.putString("emailregister", emailregister.getText().toString());
        b.putString("addressregister", addressregister.getText().toString());
        b.putString("passwordregister", passwordregister.getText().toString());
        //Añadimos la información al intent
        i.putExtras(b);
        startActivity(i);
    }

}
