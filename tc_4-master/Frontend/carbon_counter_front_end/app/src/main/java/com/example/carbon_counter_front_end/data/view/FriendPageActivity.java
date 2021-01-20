package com.example.carbon_counter_front_end.data.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.carbon_counter_front_end.R;
import com.example.carbon_counter_front_end.data.logic.AdminLogic;
import com.example.carbon_counter_front_end.data.logic.FriendPageLogic;
import com.example.carbon_counter_front_end.data.model.IVolleyListener;
import com.example.carbon_counter_front_end.data.model.RequestServerForService;
import com.example.carbon_counter_front_end.data.model.TargetUserInformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FriendPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_page);

        final TextView username = (TextView) findViewById(R.id.editTextTextPersonName);
        Button AddFriend = (Button) findViewById(R.id.btn_addfriend);
        Button Back = (Button) findViewById(R.id.button4);

        final FriendPageLogic friendpageLogic= new FriendPageLogic( FriendPageActivity.this, getApplicationContext());

        AddFriend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                TargetUserInformation.username = username.getText().toString();

                friendpageLogic.setModel(new RequestServerForService(getApplicationContext(), new IVolleyListener() {
                    @Override
                    public void onImageSuccess(Bitmap image) {

                    }
                    @Override
                    public void onSuccessJSONArray(JSONArray response) {

                    }
                    @Override
                    public void onSuccess(JSONObject response) {

                    }

                    @Override
                    public void onSuccess(String response) {

                    }

                    @Override
                    public void onError() {
                    }
                }));
                friendpageLogic.friendRequest();
            }
        });

        Back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FriendPageActivity.this, FriendListActivity.class);
                startActivity(i);
            }
        });


        }


}


