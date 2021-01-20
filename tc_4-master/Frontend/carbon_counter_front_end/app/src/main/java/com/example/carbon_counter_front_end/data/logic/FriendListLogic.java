package com.example.carbon_counter_front_end.data.logic;

import android.content.Context;

import com.example.carbon_counter_front_end.data.model.RequestServerForService;
import com.example.carbon_counter_front_end.data.model.TargetUserInformation;
import com.example.carbon_counter_front_end.data.model.UserInformation;
import com.example.carbon_counter_front_end.data.view.FriendListActivity;


public class FriendListLogic {


    private RequestServerForService model;
    private FriendListActivity view;
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
    public FriendListLogic(FriendListActivity view, Context context) {
        this.view = view;
        this.context = context;
    }

    public void setModel(RequestServerForService m) {
        this.model = m;
    }



    public void friends() {
        String url = "http://10.24.227.38:8080/user/friend_list";
      //  url += "/" + UserInformation.username;
       // url+= "?username="+ TargetUserInformation.username;
        model.contactServerArray(url);
    }
}
