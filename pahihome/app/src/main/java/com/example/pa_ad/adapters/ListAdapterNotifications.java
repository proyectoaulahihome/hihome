package com.example.pa_ad.adapters;

import android.app.Notification;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pa_ad.R;
import com.example.pa_ad.models.NotificationsModel;

import java.util.List;

public class ListAdapterNotifications  extends BaseAdapter {

    Context context;
    List<NotificationsModel> lst;

    public ListAdapterNotifications(Context context, List<NotificationsModel> lst) {
        this.context = context;
        this.lst = lst;
    }

    @Override
    public int getCount() {
        return lst.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageViewNotification;
        TextView textViewHeader;
        TextView textViewBody;

        NotificationsModel notificationsModel= lst.get(i);

        if (view==null)
            view= LayoutInflater.from(context).inflate(R.layout.listview_notification,null);

        imageViewNotification=view.findViewById(R.id.imageViewNotification);
        textViewHeader=view.findViewById(R.id.textViewHeader);
        textViewBody=view.findViewById(R.id.textViewBody);

        imageViewNotification.setImageResource(notificationsModel.imagen);
        textViewHeader.setText(notificationsModel.nombre);
        textViewBody.setText(notificationsModel.Des);
        return view;
    }
}
