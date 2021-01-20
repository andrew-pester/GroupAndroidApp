package com.example.carbon_counter_front_end.data.logic;

import android.content.Context;

import com.example.carbon_counter_front_end.data.model.RequestServerForService;
import com.example.carbon_counter_front_end.data.model.UserInformation;
import com.example.carbon_counter_front_end.data.view.TipCategoryActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Stack;

/**
 * Logic class to take care of the logic for TipCategoryActivity
 * @author Zachary Current
 */
public class TipCategoryLogic {
    private RequestServerForService model;
    private TipCategoryActivity view;
    private Context context;
    private int milesDriven;
    private int water;
    private int power;
    private int meat;
    private int garbage;

    /**
     * TipCategoryLogic Constructor - to take care of the TipCategoryActivity logic
     * @param view view of TipCategoryActivity
     * @param context context of TipCategoryActivity
     */
    public TipCategoryLogic(TipCategoryActivity view, Context context){
        this.view = view;
        this.context = context;
    }

    /**
     * To set the model of this logic to take care of contacting the server.
     * @param m model to contact the server
     */
    public void setModel(RequestServerForService m){ this.model = m; }

    /**
     * Contacts the server and calls setLayout(response) on successful connection
     */
    public void contactServer() {
        String url = "http://10.24.227.38:8080/stats/today";

        url += "/" + UserInformation.username;


        model.contactServer(url);
    }


    private void setStats(JSONObject response) {
        try {
            milesDriven = response.getInt("milesDriven");
            water = response.getInt("water");
            power = response.getInt("power");
            meat = response.getInt("meat");
            garbage = response.getInt("garbage");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Called on successful contact to the server. Sets the stats of the user
     * Formats a stack of characters with the order of how TipCategoryActivity should be layed out
     * @param response response from server
     * @return Stack with the order of the layout
     */
    public Stack<Character> setLayout(JSONObject response) {
        setStats(response);

        final int milesIndex = 0;
        final int waterIndex = 1;
        final int powerIndex = 2;
        final int garabageIndex = 3;

        Stack<Character> recommendStack = new Stack<Character>();
        Stack<Character> allStack = new Stack<Character>();



        if(milesDriven > 100){
            recommendStack.push('m');
        } else {
            allStack.push('m');
        }

        if(water > 100){
            recommendStack.push('w');
        } else {
            allStack.push('w');
        }

        if(power > 100){
            recommendStack.push('p');
        } else {
            allStack.push('p');
        }

        if(garbage > 100){
            recommendStack.push('g');
        } else {
            allStack.push('g');
        }

        allStack.push('a');

        recommendStack.addAll(0, allStack);


        return recommendStack;

    }
}
