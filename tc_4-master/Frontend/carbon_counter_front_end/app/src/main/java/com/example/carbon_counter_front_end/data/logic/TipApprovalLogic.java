package com.example.carbon_counter_front_end.data.logic;

import android.content.Context;

import com.example.carbon_counter_front_end.data.model.RequestServerForService;
import com.example.carbon_counter_front_end.data.view.TipApprovalActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TipApprovalLogic {
    private RequestServerForService model;
    private TipApprovalActivity view;
    private Context context;
    private ArrayList<JSONObject> myTips;
    private int tipsIndex;

    /**
     * ViewCategoryResultsLogic Constuctor - to take care of the logic for ViewCategoryResults
     * @param view view of ViewCategoryResults
     * @param context context of ViewCategoryResults
     */
    public TipApprovalLogic(TipApprovalActivity view, Context context){
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
     *
     */
    public void getTips(){
        String url = "http://10.24.227.38:8080/tips/all/admin";

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
                subject = myTips.get(tipsIndex).getString("workingTitle");
            } else {
                subject = "No tips to approve";
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
                description = myTips.get(tipsIndex).getString("workingBody");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return description;
    }

    public void approveTip(){
        if(myTips.size() == 0){
            return;
        }

        String url = "http://10.24.227.38:8080/tip/setStatus/";


        String title = getSubject();
        url +=  title + "?newStatus=APPROVED";


        model.contactServerString(url);


    }

    public void deleteTip(){
        if(myTips.size() == 0){
            return;
        }
        String url = "http://10.24.227.38:8080/tip/setStatus/";


        String title = getSubject();
        url +=  title + "?newStatus=DENIED";


        model.contactServerString(url);
    }
}
