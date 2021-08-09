package com.example.pa_ad.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pa_ad.R;
import com.example.pa_ad.RetrofitClient;
import com.example.pa_ad.RetrofitClientPython;
import com.example.pa_ad.activitys.MainActivity;
import com.example.pa_ad.models.DataModel;
import com.example.pa_ad.models.NotificationsModel;
import com.example.pa_ad.models.ReconocimientoModel;
import com.example.pa_ad.models.UserModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RealTimeControlFragment extends Fragment {
    PieChart piechart;
    TextView txtmqgas, txtmlx, txtmqhumo;
    private ProgressDialog proDialog;
    List<DataModel> dataModel;
    // variables para mantener sesion
    private SharedPreferences preferences;
    private String user_id, name, last_name, email, address, type, imguser;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.realtimecontrol_fragment,container,false);
        piechart = (PieChart)view.findViewById(R.id.Pastelchart);
        txtmqgas =  (TextView) view.findViewById(R.id.txtmqgas);
        txtmlx =  (TextView) view.findViewById(R.id.txtmlx);
        txtmqhumo =  (TextView) view.findViewById(R.id.txtmqhumo);
        init();
        sessionuser();
        exec();
        return view;
    }

    private void exec(){
        final Handler handler= new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                grafi();//llamamos nuestro metodo
                handler.postDelayed(this,1000);//se ejecutara cada 10 segundos
            }
        },5000);//empezara a ejecutarse después de 5 milisegundos
    }
    private void grafi(){

        if(user_id != null && email != null){
            Description descrio=new Description();
            //descrio.setText("Actividad");
            piechart.setDescription(descrio);
            Call<List<DataModel>> call = RetrofitClient.getInstance().getApi().fetchdatamodel();
            call.enqueue(new Callback<List<DataModel>>() {
                @Override
                public void onResponse(Call<List<DataModel>> call, Response<List<DataModel>> response) {

                    ArrayList<PieEntry> pieEntries=new ArrayList<>();
                    JSONArray jsArray = new JSONArray(response.body());
                    for(int i=0; i < jsArray.length(); i++){
                        try {
                            JSONObject x = jsArray.getJSONObject(i);
                            txtmqgas.setText(String.valueOf(x.get("mqgas").toString()));
                            txtmlx.setText(String.valueOf(x.get("mlx").toString()));
                            txtmqhumo.setText(String.valueOf(x.get("mqhumo").toString()));
                            pieEntries.add(new PieEntry( 1, String.valueOf(x.get("mqgas").toString())));
                            pieEntries.add(new PieEntry( 2, String.valueOf(x.get("mlx").toString())));
                            pieEntries.add(new PieEntry( 3, String.valueOf(x.get("mqhumo").toString())));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    // pieEntries.add(new PieEntry( tres, 7));
                    PieDataSet pieDataSet=new PieDataSet(pieEntries, "MQGAS" +"-"+ "MLX" +"-"+ "MQHUMO");
                    pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                    PieData pieData=new PieData(pieDataSet);
                    piechart.setData(pieData);
                }

                @Override
                public void onFailure(Call<List<DataModel>> call, Throwable t) {

                }
            });
        }else{
            Toast.makeText(getActivity(), "No session", Toast.LENGTH_LONG).show();
            gologin();
        }


    }


   /* private List<DataModel> GetData() {
        dataModel=new ArrayList<>();
        dataModel.add(new DataModel("78","0","0"));
        dataModel.add(new DataModel("67","10","0"));
        dataModel.add(new DataModel("30","10","0"));
        dataModel.add(new DataModel("10","33","0"));
        dataModel.add(new DataModel("10","20","0"));
        dataModel.add(new DataModel("20","20","0"));
        dataModel.add(new DataModel("1000","20","0"));

        return dataModel;
    } */
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


    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}