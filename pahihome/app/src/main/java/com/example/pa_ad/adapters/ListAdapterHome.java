package com.example.pa_ad.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pa_ad.R;
import com.example.pa_ad.models.UserModel;

import java.util.List;


public class ListAdapterHome extends RecyclerView.Adapter<ListAdapterHome.HomeViewHolder> implements  View.OnClickListener{

    private List<UserModel> mData;
    private LayoutInflater mInflater;
    private Context context;
    private View.OnClickListener listener;

    public ListAdapterHome(List<UserModel> mData, Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = mData;
        this.context = context;
    }

    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       // View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_list_home,null,false);
        View view = mInflater.inflate(R.layout.element_list_home, parent,false);
        view.setOnClickListener(this);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapterHome.HomeViewHolder holder, int position) {
        holder.name.setText(mData.get(position).getName());
        holder.email.setText(mData.get(position).getEmail());
        holder.address.setText(mData.get(position).getAddress());
        Glide.with(context).load(mData.get(position).getImguser()).into(holder.HomeImageViewUser);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
     this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if(listener!=null){
            listener.onClick(view);
        }
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder{
        ImageView HomeImageViewUser;
        TextView user_id, name, last_name,email,password,address,type,imguser;

        HomeViewHolder (View itemView){
            super(itemView);
            HomeImageViewUser = itemView.findViewById(R.id.HomeImageViewUser);
            name = itemView.findViewById(R.id.homenameuser);
            email = itemView.findViewById(R.id.homeemailuser);
            address = itemView.findViewById(R.id.homeaddressuser);
        }
    }
}
