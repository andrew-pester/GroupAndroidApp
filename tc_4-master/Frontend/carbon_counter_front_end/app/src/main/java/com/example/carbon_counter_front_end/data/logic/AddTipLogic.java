package com.example.carbon_counter_front_end.data.logic;

import android.content.Context;

import com.example.carbon_counter_front_end.data.model.RequestServerForService;
import com.example.carbon_counter_front_end.data.view.AddTipActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Logic class to take care of the logic for AddTipActivity
 * @author Zachary Current
 */
public class AddTipLogic {
    private RequestServerForService model;
    private AddTipActivity view;
    private Context context;

    /**
     * AddTipLogic Constructor - to take care of the logic for the provided AddTipActivity
     * @param view view of AddTipActivity
     * @param context context of AddTipActivity
     */
    public AddTipLogic(AddTipActivity view, Context context){
        this.view = view;
        this.context = context;
    }

    /**
     * Set the model to take care of the server requests
     * @param m model to handle server requests
     */
    public void setModel(RequestServerForService m) { this.model = m; }

    /**
     * Add tip to server, will be unapproved
     * @param category category of tip
     * @param description description of tip
     * @param subject subject of tip
     */
    public void addTip(String category, String description, String subject){
        String url = "http://10.24.227.38:8080/tip/addTip";

        JSONObject newTip = new JSONObject();
        try {
            newTip.put("category", category);
            newTip.put("body", description);
            newTip.put("title", subject);

            model.postServer(url, newTip);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
