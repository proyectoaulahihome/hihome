package com.example.pa_ad.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.pa_ad.R;
import com.example.pa_ad.models.NotificationsModel;
import com.example.pa_ad.models.UserModel;

public class DetailNotificationFragment extends Fragment {
    TextView txtHeader;
    TextView txtBody;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.home_detail_fragment, container, false);
        return view;
    }
}
