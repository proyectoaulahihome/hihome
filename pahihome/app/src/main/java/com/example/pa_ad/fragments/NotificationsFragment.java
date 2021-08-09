package com.example.pa_ad.fragments;

import android.os.Bundle;
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

import com.example.pa_ad.R;
import com.example.pa_ad.adapters.ListAdapterNotifications;
import com.example.pa_ad.interfaces.iCommunicates_Fragments;
import com.example.pa_ad.models.NotificationsModel;
import com.example.pa_ad.models.UserModel;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {
    iCommunicates_Fragments interfacecommunicates_Fragments;
    NotificationsModel notificationsModel;
    List<NotificationsModel> ListElementsnotifications;
    ListView ListViewNotification;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications,container,false);
        ListViewNotification = (ListView) view.findViewById(R.id.notifications_listview);

        ListAdapterNotifications listAdapterNotifications =
                new ListAdapterNotifications(getContext(), GetData());
        ListViewNotification.setAdapter(listAdapterNotifications);

        ListViewNotification.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               NotificationsModel notificationsModel = ListElementsnotifications.get(position);
                Toast.makeText(getActivity(),notificationsModel.Des,Toast.LENGTH_SHORT).show();
            }
        });
        return view;
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
}