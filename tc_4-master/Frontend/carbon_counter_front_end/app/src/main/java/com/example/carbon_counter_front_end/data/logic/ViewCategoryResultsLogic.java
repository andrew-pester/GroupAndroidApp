package com.example.carbon_counter_front_end.data.logic;

import android.content.Context;

import com.example.carbon_counter_front_end.data.model.RequestServerForService;
import com.example.carbon_counter_front_end.data.view.ViewCategoryResults;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Logic class to take care of the logic for ViewCategoryResults Activity.
 * @author Zachary Current
 */
public class ViewCategoryResultsLogic {
    private RequestServerForService model;
    private ViewCategoryResults view;
    private Context context;
    private ArrayList<JSONObject> myTips;
    private int tipsIndex;

    /**
     * ViewCategoryResultsLogic Constuctor - to take care of the logic for ViewCategoryResults
     * @param view view of ViewCategoryResults
     * @param context context of ViewCategoryResults
     */
    public ViewCategoryResultsLogic(ViewCategoryResults view, Context context){
        this.view = view;
        this.context = context;
        this.myTips = new ArrayList<>();
        this.tipsIndex = 0;
    }

    /**
     * Set model for ViewCategoryResults
     * @param m - model to handle server requests
     */
    public void setModel(RequestServerForService m){ this.model = m; }

    /**
     * Used to contact the server, the response returns the tips based on the category supplied, if valid.
     * @param category category of tips to be accessed
     */
    public void getTips(String category){
        String url = "http://10.24.227.38:8080/tips";

        url += "/" + category;

        model.contactServerArray(url);
    }

    /**
     * On successful connection, adds each tips object to myTips list
     * @param response JSONArray of tips for the category provided to getTips(category)
     */
    public void setTips(JSONArray response){
        for(int i = 0; i < response.length(); i++){
            try {
                myTips.add((JSONObject) response.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Called when next is pressed, increments tipIndex
     */
    public void setNextTip(){
        if(myTips.size() == 0){
            return;
        }

        if(tipsIndex == myTips.size() - 1){
            tipsIndex = 0;
        } else {
            tipsIndex++;
        }
    }

    /**
     * Called when prev is pressed, decrements tipIndex
     */
    public void setPrevTip(){
        if(myTips.size() == 0){
            return;
        }

        if(tipsIndex == 0){
            tipsIndex = myTips.size() - 1;
        } else {
            tipsIndex--;
        }
    }

    /**
     * Called to get the subject of the current tips object to be displayed on ViewCategoryResults
     * @return subject of current tips object
     */
    public String getSubject(){
        String subject = "";

        try {
            if(myTips.size() > 0) {
                subject = myTips.get(tipsIndex).getString("title");
            } else {
                subject = "No tips to be displayed";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return subject;

    }

    /**
     * Called to get the description of the current tips object to be displayed on ViewCategoryResults
     * @return description of current tips object
     */
    public String getDescription(){
        String description = "";

        try {
            if(myTips.size() > 0) {
                description = myTips.get(tipsIndex).getString("body");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return description;
    }

    public void deleteTip(){
        if(myTips.size() == 0){
            return;
        }

        String url = "http://10.24.227.38:8080/tip/delete/";

        try {
            String title = myTips.get(tipsIndex).getString("workingTitle");
            url += title;

            model.contactServerString(url);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
