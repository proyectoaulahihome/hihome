package com.example.pa_ad.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
import com.example.pa_ad.activitys.MainActivity;
import com.example.pa_ad.adapters.ListAdapterNotifications;
import com.example.pa_ad.interfaces.iCommunicates_Fragments;
import com.example.pa_ad.models.NotificationsModel;
import com.example.pa_ad.models.UserModel;
import com.github.mikephil.charting.data.BarEntry;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationsFragment extends Fragment {
    private String URL = "https://bsmarthome.herokuapp.com/webresources/";
    private RequestQueue requestQueue;
    private ProgressDialog proDialog;
    iCommunicates_Fragments interfacecommunicates_Fragments;
    NotificationsModel notificationsModel;
    List<NotificationsModel> ListElementsnotifications;
    ListView ListViewNotification;
    private SharedPreferences preferences;
    private String user_id, name, last_name, email, address, type, imguser;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications,container,false);


        init();
        proDialog = new ProgressDialog(getActivity());
        proDialog.setTitle("Data");
        proDialog.setMessage("Loading data please wait...");
        proDialog.show();
        sessionuser();


        return view;
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
    }
    private void gologin() {
        Intent i = new Intent(getActivity(), MainActivity.class);
        // bandera para que no se creen nuevas actividades innecesarias
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    private List<NotificationsModel> GetData() {
        ListElementsnotifications=new ArrayList<>();
        ListElementsnotifications.add(new NotificationsModel(1,R.drawable.notifications,"Nuevo ingreso al hogar","Ingresó el día de hoy"));
        ListElementsnotifications.add(new NotificationsModel(2,R.drawable.notifications,"Nuevo ingreso al hogar","Ingresó el día de hoy"));
        ListElementsnotifications.add(new NotificationsModel(3,R.drawable.notifications,"Nuevo ingreso al hogar","Ingresó el día de hoy"));
        ListElementsnotifications.add(new NotificationsModel(4,R.drawable.notifications,"Nuevo ingreso al hogar","Ingresó el día de ayer"));
        ListElementsnotifications.add(new NotificationsModel(5,R.drawable.notifications,"Nuevo ingreso al hogar","Ingresó el día de ayer"));
        ListElementsnotifications.add(new NotificationsModel(6,R.drawable.notifications,"Nuevo ingreso al hogar","Ingresó hace 2 días"));
        ListElementsnotifications.add(new NotificationsModel(7,R.drawable.notifications,"Nuevo ingreso al hogar","Ingresó hace 2 días"));

        return ListElementsnotifications;
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(user_id != null && email != null){
            StringRequest request = new StringRequest(
                    Request.Method.GET,
                    URL + "notification",
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            ListElementsnotifications=new ArrayList<>();
                            proDialog.dismiss();
                            int size = response.length();
                            boolean band = false;
                            response = fixEncoding(response);
                              Log.d("Respuesta", response);
                            try {
                                JSONObject json_transform = null;

                                JSONArray jsonarray = new JSONArray(response);
                                for(int i=0; i < jsonarray.length(); i++)
                                {
                                    JSONObject jsonObject = new JSONObject(jsonarray.get(i).toString());
                                    ListElementsnotifications.add(new NotificationsModel(jsonObject.getInt("notification_id"),  R.drawable.notifications,
                                            jsonObject.getString("title"), jsonObject.getString("message") + ", a las " + jsonObject.getString("date_notification")));
                                }

                                ListViewNotification = (ListView) view.findViewById(R.id.notifications_listview);
                                ListAdapterNotifications listAdapterNotifications =
                                        new ListAdapterNotifications(getContext(), ListElementsnotifications);
                                ListViewNotification.setAdapter(listAdapterNotifications);

                                ListViewNotification.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        NotificationsModel notificationsModel = ListElementsnotifications.get(position);
                                        Toast.makeText(getActivity(),notificationsModel.Des,Toast.LENGTH_SHORT).show();
                                    }
                                });





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
            };
            if (requestQueue == null) {
                requestQueue = Volley.newRequestQueue(getActivity());
                requestQueue.add(request);
            } else {
                requestQueue.add(request);
            }
        } else{
            Toast.makeText(getActivity(), "No session", Toast.LENGTH_LONG).show();
            gologin();
        }
    }


}