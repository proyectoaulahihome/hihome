package com.example.pa_ad.activitys;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
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
import com.example.pa_ad.interfaces.iCommunicates_Fragments;
import com.example.pa_ad.models.NotificationsModel;
import com.example.pa_ad.models.UserModel;
import com.google.android.material.navigation.NavigationView;

public class principal_activity_main extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, iCommunicates_Fragments {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    //variable del fragment detallehome
    DetailHomeFragment detailHomeFragment;
    //variable del fragment detalleNotifications
    DetailNotificationFragment detailNotificationFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal_activity_main);
        Bundle bundle = this.getIntent().getExtras();
        if(bundle != null){
                toolbar = findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                drawerLayout = findViewById(R.id.drawerLayout);
                navigationView = findViewById(R.id.navigationView);
                //implementa luego de haber implementado NavigationView.OnNavigationItemSelectedListener
                // evento onclick
                navigationView.setNavigationItemSelectedListener(this);
                // añadiendo imagenes full color
                navigationView.setItemIconTintList(null);

                View hView = navigationView.getHeaderView(0);
                ImageView foto = (ImageView) hView.findViewById(R.id.imageProfile);
                TextView Usuario = (TextView) hView.findViewById(R.id.viewNameUser);
                TextView RolUsuario = (TextView) hView.findViewById(R.id.viewRol);
                Usuario.setText(bundle.getString("name") + " " + bundle.getString("last_name"));
                RolUsuario.setText(bundle.getString("type"));
                Glide.with(this).load(bundle.getString("imguser").replace('\\', '/')).into(foto);
                actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
                drawerLayout.addDrawerListener(actionBarDrawerToggle);
                actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
                actionBarDrawerToggle.syncState();

                //cargar fragment principal en la actividad
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.container,new HomeFragment());
                fragmentTransaction.commit();
            }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        drawerLayout.closeDrawer(GravityCompat.START);
        if(menuItem.getItemId() == R.id.menuHome){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container,new HomeFragment());
            fragmentTransaction.commit();
        }
        if(menuItem.getItemId() == R.id.menuNotificationsAdm){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container,new NotificationsFragment());
            fragmentTransaction.commit();
        }
        return false;
    }

    @Override
    public void SendHome(UserModel usermodel) {
        // aquí se realiza toda la lógica necesaria para poder realizar el envio
        detailHomeFragment = new DetailHomeFragment();
        // object bundle para transportar la información
        Bundle bundleSend = new Bundle();
        // enviar el objeto que está llegando con Serializable
        bundleSend.putSerializable("object", usermodel);
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
}

