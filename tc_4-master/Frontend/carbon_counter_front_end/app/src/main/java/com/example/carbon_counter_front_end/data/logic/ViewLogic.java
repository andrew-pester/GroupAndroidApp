package com.example.carbon_counter_front_end.data.logic;

import android.content.Context;
import android.widget.TextView;

import com.example.carbon_counter_front_end.data.model.RequestServerForService;
import com.example.carbon_counter_front_end.data.model.UserInformation;
import com.example.carbon_counter_front_end.data.view.ViewActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class ViewLogic {

    private RequestServerForService model;
    private ViewActivity view;
    private Context context;

    private int milesDriven;
    private int water;
    private int power;
    private int meat;
    private int garbage;

    public ViewLogic(ViewActivity view, Context context){
        this.view = view;
        this.context = context;
    }

    public void setModel(RequestServerForService m) { this.model = m;}

    public void getMonthlyStats(){
        String url = "http://10.24.227.38:8080/stats/lastMonth";
        url += "/" + UserInformation.username;
        model.contactServerArray(url);
    }
}