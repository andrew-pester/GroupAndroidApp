package com.example.carbon_counter_front_end.data.logic;

import android.content.Context;

import com.example.carbon_counter_front_end.data.model.RequestServerForService;
import com.example.carbon_counter_front_end.data.model.TargetUserInformation;
import com.example.carbon_counter_front_end.data.model.UserInformation;
import com.example.carbon_counter_front_end.data.view.FreindRequestsActivity;

public class FriendRequestsLogic {

    private RequestServerForService model;
    private FreindRequestsActivity view;
    private Context context;
    //localhost:8080/user/deny/test6?userOne=andrew
    //localhost:8080/user/requests/andrew
    //localhost:8080/user/accept/test6?userOne=andrew
    //http:// 10.24.227.38:8080/user/friend_request/andrew?username=test6
    /**
     * Friend Request  Constructor - To take care of logic for the FriendRequestsActivity
     *
     *
     * @param view    view of AdminUpdateUser
     * @param context context of AdminUpdateUser
     */
    public FriendRequestsLogic(FreindRequestsActivity view, Context context) {
        this.view = view;
        this.context = context;
    }

    public void setModel(RequestServerForService m) {
        this.model = m;
    }

    public void AcceptRequest() {
        String url = "http://10.24.227.38:8080/user/accept";
        url += "/" + UserInformation.username;
        url+= "?userOne="+ TargetUserInformation.username;
        model.contactServer(url);
    }

    public void DenyRequest() {
        String url = "http://10.24.227.38:8080/user/deny";
        url += "/" + UserInformation.username;
        url+= "?userOne="+ TargetUserInformation.username;
        model.contactServer(url);
    }
    public void Allrequests() {
        String url = "http://10.24.227.38:8080/user/requests";
        url += "/" + UserInformation.username;
        // url+= "?username="+ TargetUserInformation.username;
        model.contactServerArray(url);
    }
}
