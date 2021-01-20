package com.example.carbon_counter_front_end.data.logic;

import android.content.Context;

import com.example.carbon_counter_front_end.data.model.RequestServerForService;
import com.example.carbon_counter_front_end.data.model.TargetUserInformation;
import com.example.carbon_counter_front_end.data.view.AdminUpdateUser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Logic Utilized in Admin Views
 * @author Morgan Funk
 */
public class AdminLogic {

    private RequestServerForService model;
    private AdminUpdateUser view;
    private Context context;
    /**
     * AdminLogic Constructor - To take care of logic for the loginActivity
     * @param view view of AdminUpdateUser
     * @param context context of AdminUpdateUser
     */
    public AdminLogic(AdminUpdateUser view, Context context){
        this.view = view;
        this.context = context;
    }

    public void setModel(RequestServerForService m) { this.model = m;}

    public void authenticate(JSONObject stats) {
        // String url = "http://10.24.227.38:8080/stats/addDaily";
        String url = "http://10.24.227.38:8080/user/edit";
        url += "/" + TargetUserInformation.username;
        try {
            model.postServer(url, stats);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void DeleteUser()
    {
        String url = "http://10.24.227.38:8080/user";
        url += "/" +TargetUserInformation.username;
        model.deleteRequest(url);
    }

    public void getUser()
    {
        String url = "http://10.24.227.38:8080/user";
        url += "/" + TargetUserInformation.username;
        model.contactServer(url);
    }


}
