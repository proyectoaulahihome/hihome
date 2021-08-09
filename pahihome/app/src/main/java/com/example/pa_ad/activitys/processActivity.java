package com.example.pa_ad.activitys;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.pa_ad.R;
import com.example.pa_ad.fragments.DetailHomeFragment;
import com.example.pa_ad.fragments.DetailNotificationFragment;
import com.example.pa_ad.fragments.HomeFragment;
import com.example.pa_ad.fragments.NotificationsFragment;
import com.example.pa_ad.fragments.RealTimeControlFragment;
import com.example.pa_ad.interfaces.iCommunicates_Fragments;
import com.example.pa_ad.models.NotificationsModel;
import com.example.pa_ad.models.UserModel;
import com.google.android.material.navigation.NavigationView;

public class processActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, iCommunicates_Fragments {

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
private String user_id, name, last_name, email, address, type, imguser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.process_activity);

        init();
        sessionuser();

        if(user_id != null && email != null){

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

            actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
            drawerLayout.addDrawerListener(actionBarDrawerToggle);
            actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
            actionBarDrawerToggle.syncState();
            fragmentManager = getSupportFragmentManager();//cargar fragment principal en la actividad
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.container,new HomeFragment());
            fragmentTransaction.commit();
        }else{
            Toast.makeText(processActivity.this, "No session", Toast.LENGTH_LONG).show();
            gologin();
        }
    }

    private void init(){
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
    private void logoff(){
        preferences.edit().clear().apply();
        gologin();
        Toast.makeText(processActivity.this, "Closed session", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        drawerLayout.closeDrawer(GravityCompat.START);

        switch(menuItem.getItemId()) {
            case R.id.menuHome:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container,new HomeFragment());
                fragmentTransaction.commit();
                Toast.makeText(processActivity.this, "Home", Toast.LENGTH_LONG).show();
                break;
            case R.id.menuNotificationsAdm:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container,new NotificationsFragment());
                fragmentTransaction.commit();
                Toast.makeText(processActivity.this, "Notifications", Toast.LENGTH_LONG).show();
                break;
            case R.id.menurealtimecontrol:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container,new RealTimeControlFragment());
                fragmentTransaction.commit();
                Toast.makeText(processActivity.this, "Real-time Control", Toast.LENGTH_LONG).show();
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
        if (keyCode == event.KEYCODE_BACK){
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
}

