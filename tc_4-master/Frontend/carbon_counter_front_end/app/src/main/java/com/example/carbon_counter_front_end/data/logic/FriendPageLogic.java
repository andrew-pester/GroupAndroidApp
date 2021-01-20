package com.example.carbon_counter_front_end.data.logic;

import android.content.Context;

import com.example.carbon_counter_front_end.data.model.RequestServerForService;
import com.example.carbon_counter_front_end.data.model.TargetUserInformation;
import com.example.carbon_counter_front_end.data.model.UserInformation;
import com.example.carbon_counter_front_end.data.view.AdminUpdateUser;
import com.example.carbon_counter_front_end.data.view.FriendPageActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Logic Utilized in Friend Page Views
 * @author Morgan Funk
 */
public class FriendPageLogic {

    private RequestServerForService model;
    private FriendPageActivity view;
    private Context context;
    //localhost:8080/user/deny/test6?userOne=andrew
    //localhost:8080/user/requests/andrew
    //localhost:8080/user/accept/test6?userOne=andrew
    //http:// 10.24.227.38:8080/user/friend_request/andrew?username=test6
    /**
     * FriendPage  Constructor - To take care of logic for the FriendPageActivity
     *
     *
     * @param view    view of AdminUpdateUser
     * @param context context of AdminUpdateUser
     */
    public FriendPageLogic(FriendPageActivity view, Context context) {
        this.view = view;
        this.context = context;
    }

    public void setModel(RequestServerForService m) {
        this.model = m;
    }



    public void friendRequest() {
        String url = "http://10.24.227.38:8080/user/friend_request";
        url += "/" + UserInformation.username;
        url+= "?username="+TargetUserInformation.username;
        model.contactServer(url);
    }



}