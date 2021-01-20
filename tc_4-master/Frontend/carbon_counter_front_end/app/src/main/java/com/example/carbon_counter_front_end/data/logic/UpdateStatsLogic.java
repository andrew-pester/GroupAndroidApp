package com.example.carbon_counter_front_end.data.logic;

import android.content.Context;
import android.widget.TextView;

import com.example.carbon_counter_front_end.data.model.RequestServerForService;
import com.example.carbon_counter_front_end.data.model.UserInformation;
import com.example.carbon_counter_front_end.data.view.UpdateActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateStatsLogic {
    private RequestServerForService model;
    private UpdateActivity view;
    private Context context;

    public UpdateStatsLogic(UpdateActivity view, Context context){
        this.view = view;
        this.context = context;
    }

    public void setModel(RequestServerForService m) { this.model = m;}

    public void authenticate(JSONObject stats) {
        // String url = "http://10.24.227.38:8080/stats/addDaily";
        String url = "http://10.24.227.38:8080/stats/addDaily";
        //url += "/" + UserInformation.username;
        try {
            model.postServer(url, stats);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
