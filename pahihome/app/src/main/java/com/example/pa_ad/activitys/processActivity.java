package com.example.pa_ad.activitys;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.pa_ad.R;
import com.example.pa_ad.fragments.DetailHomeFragment;
import com.example.pa_ad.fragments.DetailNotificationFragment;
import com.example.pa_ad.fragments.HomeFragment;
import com.example.pa_ad.fragments.LinkuserFragment;
import com.example.pa_ad.fragments.NotificationsFragment;
import com.example.pa_ad.fragments.RealTimeControlFragment;
import com.example.pa_ad.interfaces.iCommunicates_Fragments;
import com.example.pa_ad.models.NotificationsModel;
import com.example.pa_ad.models.UserModel;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class processActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, iCommunicates_Fragments {

    //private String URL = "https://bsmarthome.herokuapp.com/webresources";
    private String URL = "http://aplicaciones.uteq.edu.ec/bsmarthome/webresources/";
    private RequestQueue requestQueue;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    //variable del fragment detallehome
    private DetailHomeFragment detailHomeFragment;
    //variable del fragment detalleNotifications
    private DetailNotificationFragment detailNotificationFragment;
    // variables para mantener sesion
    private SharedPreferences preferences;
    private String user_id, name, last_name, email, address, type, imguser,device_id;

    private Handler handlernotify;
    private Runnable mTickernotify;

    // notificacion activa o inactiva
    private SharedPreferences preferencenotification;
    String notificationac;

    // notificaciones
    private PendingIntent pendingIntent;
    private final static String CHANNEL_ID = "NOTIFICACION";
    private final static int NOTIFICACION_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.process_activity);

        init();
        sessionuser();
        notificationpush();

        if (user_id != null && email != null) {

            execnotify();
            toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            drawerLayout = findViewById(R.id.drawerLayout);
            navigationView = findViewById(R.id.navigationView);
            //implementa luego de haber implementado NavigationView.OnNavigationItemSelectedListener
            // evento onclick
            navigationView.setNavigationItemSelectedListener(this);
            // añadiendo imagenes full color
            navigationView.setItemIconTintList(null);
            // para añadir datos del usuario al menu encabezado
            View hView = navigationView.getHeaderView(0);
            ImageView foto = (ImageView) hView.findViewById(R.id.imageProfile);
            TextView Usuario = (TextView) hView.findViewById(R.id.viewNameUser);
            TextView RolUsuario = (TextView) hView.findViewById(R.id.viewRol);
            Usuario.setText(name + " " + last_name);
            RolUsuario.setText(type);
            Glide.with(this).load(imguser.replace('\\', '/')).into(foto);

            actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
            drawerLayout.addDrawerListener(actionBarDrawerToggle);
            actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
            actionBarDrawerToggle.syncState();

            if(notificationac!=null){
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container,new RealTimeControlFragment());
                fragmentTransaction.commit();
                Toast.makeText(processActivity.this, "Real-time Control", Toast.LENGTH_LONG).show();
                notificationpushclear();
            }else{
                fragmentManager = getSupportFragmentManager();//cargar fragment principal en la actividad
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.container, new HomeFragment());
                fragmentTransaction.commit();
            }

        } else {
            Toast.makeText(processActivity.this, "No session", Toast.LENGTH_LONG).show();
            gologin();
        }
    }

    private void init() {
        preferences = getSharedPreferences("Preferences", MODE_PRIVATE);
        preferencenotification = getSharedPreferences("preferencenotification", MODE_PRIVATE);
    }

    private void gologin() {
        Intent i = new Intent(this, MainActivity.class);
        // bandera para que no se creen nuevas actividades innecesarias
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    public void sessionuser() {
        user_id = preferences.getString("user_id", null);
        name = preferences.getString("name", null);
        last_name = preferences.getString("last_name", null);
        email = preferences.getString("email", null);
        address = preferences.getString("address", null);
        type = preferences.getString("type", null);
        imguser = preferences.getString("imguser", null);
        device_id= preferences.getString("device_id",null);
    }

    public void notificationpush(){
        notificationac = preferencenotification.getString("notification", null);
    }
    private void notificationpushclear(){
        preferencenotification.edit().clear().apply();
        Toast.makeText(processActivity.this, "Clearing notification variable", Toast.LENGTH_LONG).show();}

    private void logoff() {
        preferences.edit().clear().apply();
        gologin();
        Toast.makeText(processActivity.this, "Closed session", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        drawerLayout.closeDrawer(GravityCompat.START);

        switch (menuItem.getItemId()) {
            case R.id.menuHome:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, new HomeFragment());
                fragmentTransaction.commit();
                Toast.makeText(processActivity.this, "Home", Toast.LENGTH_LONG).show();

                break;
            case R.id.menuNotificationsAdm:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, new NotificationsFragment());
                fragmentTransaction.commit();
                Toast.makeText(processActivity.this, "Notifications", Toast.LENGTH_LONG).show();

                break;
            case R.id.menurealtimecontrol:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, new RealTimeControlFragment());
                fragmentTransaction.commit();
                Toast.makeText(processActivity.this, "Real-time Control", Toast.LENGTH_LONG).show();

                break;
            case R.id.menuuserlink:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, new LinkuserFragment());
                fragmentTransaction.commit();
                Toast.makeText(processActivity.this, "Link User", Toast.LENGTH_LONG).show();

                break;
            case R.id.logOff:
                logoff();// vaciar las variables de session
                Toast.makeText(processActivity.this, "Log off", Toast.LENGTH_LONG).show();
                break;
        }
        return false;
    }

    @Override
    public void SendHome(UserModel usermodel) {
        detailHomeFragment = new DetailHomeFragment();// aquí se realiza toda la lógica necesaria para poder realizar el envio
        Bundle bundleSend = new Bundle();// object bundle para transportar la información
        bundleSend.putSerializable("object", usermodel);// enviar el objeto que está llegando con Serializable
        detailHomeFragment.setArguments(bundleSend);
        // abrir fragment
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, detailHomeFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void SenNotification(NotificationsModel notificationsModel) {

    }

    // Se controla la pulsación del botón atrás
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you want to exit hiHome?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            logoff();
                            finish();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.show();
        }
        return super.onKeyDown(keyCode, event);
    }

    //notificaciones

    private void execnotify() {
        handlernotify = new Handler();
        mTickernotify = new Runnable() {
            @Override
            public void run() {
                notifi();
                handlernotify.postDelayed(this, 2000);//se ejecutara cada 1 segundos
            }
        };
        handlernotify.postDelayed(mTickernotify, 5000);//se ejecutara cada 5 segundos
    }

    private void goRealTime(){

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

    private void notifi() {
        if(user_id != null && email != null){
            String datajson = "{\n" +
                    "   \"device_id\":\""+device_id+"\"\n" +
                    "}";

            StringRequest request = new StringRequest(
                    Request.Method.POST,
                    URL +"data/shearchbyruserdeviceAll",
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            int size = response.length();
                            boolean band = false;
                            response = fixEncoding(response);
                            //  Log.d("Respuesta", response);
                            JSONObject json_transform = null;
                            try {
                                if (size > 0)
                                {
                                    json_transform = new JSONObject(response);
                                    if(json_transform.getString("flag").equals("true")){
                                        JSONArray jsonArray = json_transform.getJSONArray("data");
                                        //   Log.d("jsonArray",jsonArray.toString());
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject object = jsonArray.getJSONObject(i);

                                            if(object.getInt("mlx") >= 36){
                                                sendMyNotification("Notification por temperatura corporal alta",
                                                        "La temperatura corporal es de: " + object.getString("mqgas")
                                                        , object.getString("data_id"));
                                                createNotificationChannel();
                                            }
                                            if(object.getInt("mqgas") >= 200){
                                                sendMyNotification("Notification por concentración de gas alta","La concentración de gas es de: " + object.getString("mqgas")
                                                        , object.getString("data_id"));
                                                createNotificationChannel();
                                            }
                                        }
                                    }else{
                                        Toast.makeText(processActivity.this,  "No data", Toast.LENGTH_LONG).show();
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
                requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(request);
            } else {
                requestQueue.add(request);
            }
        }
    }

    private void insertnotification(String datajson) {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                URL + "notification/insertnotification",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int size = response.length();
                        boolean band = false;
                        response = fixEncoding(response);
                        //
                        try {
                            Log.d("Respuesta", response);
                            /*
                            JSONObject json_transform = null;
                            JSONArray jsonarray = new JSONArray(response);
                            for (int i = 0; i < jsonarray.length(); i++) {
                                json_transform = jsonarray.getJSONObject(i);
                                Toast.makeText(processActivity.this, json_transform.getString("information"), Toast.LENGTH_LONG).show();
                            } */
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
            requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request);
        } else {
            requestQueue.add(request);
        }
    }

    private void setPendingIntent(){
        Intent intent = new Intent(this, processActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(processActivity.class);
        stackBuilder.addNextIntent(intent);
        pendingIntent = stackBuilder.getPendingIntent(1,PendingIntent.FLAG_UPDATE_CURRENT);
        // startActivity(intent);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notificacion";
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name,
                    NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private void createNotificationTempCorporal() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),
                CHANNEL_ID);
        builder.setSmallIcon(R.drawable.notifications);
        builder.setContentTitle("Notification Android");
        builder.setContentText("Temperatura corporal alta");
        builder.setColor(Color.GREEN);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setLights(Color.MAGENTA, 1000, 1000);
        builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000, 1000});
        builder.setDefaults(Notification.DEFAULT_SOUND);
        builder.setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(NOTIFICACION_ID, builder.build());
    }


    private void sendMyNotification(String title,String message, String data_id) {

        //On click of notification it redirect to this Activity
        Intent intent = new Intent(this, processActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        SharedPreferences.Editor editor = preferencenotification.edit();
        editor.putString("notification","true");
        editor.commit();
       /* Bundle b = new Bundle();
        b.putString("notification", String.valueOf(bandnotify));
        intent.putExtras(b); */
        pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String notificationjson = "{\n" +
                "   \"data_id\":\""+data_id+"\",\n" +
                "   \"title\":\""+title+"\",\n" +
                "   \"message\":\""+message+"\"\n" +
                "}";
        Log.d("JSONNOTIFICATION",notificationjson);
        insertnotification(notificationjson);
         Uri soundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.drawable.logopa);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID)
                .setSmallIcon(R.drawable.notifications)
                .setLargeIcon(icon)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }
}


