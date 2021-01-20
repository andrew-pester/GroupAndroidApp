package com.example.carbon_counter_front_end.data.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.carbon_counter_front_end.R;
import com.example.carbon_counter_front_end.data.logic.AdminLogic;
import com.example.carbon_counter_front_end.data.logic.FriendRequestsLogic;
import com.example.carbon_counter_front_end.data.model.IVolleyListener;
import com.example.carbon_counter_front_end.data.model.RequestServerForService;
import com.example.carbon_counter_front_end.data.model.TargetUserInformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FreindRequestsActivity extends AppCompatActivity {
    List<String> requestList = new ArrayList<>();
    JSONArray friendRequests = new JSONArray();
    int pos;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freind_requests);

        Button Back = (Button) findViewById(R.id.backbutton);
        Button Accept = (Button) findViewById(R.id.acceptSelected);
        Button Deny = (Button) findViewById(R.id.denyselected);
        Button last = (Button) findViewById(R.id.lastrequest);
        Button next = (Button) findViewById(R.id.nextRequest);
        Button refresh = (Button) findViewById(R.id.bttnRefresh);
        TextView SelectedUser = (TextView) findViewById(R.id.textView6);


        final FriendRequestsLogic FRLogic= new FriendRequestsLogic( FreindRequestsActivity.this, getApplicationContext());
        FRLogic.setModel(new RequestServerForService(getApplicationContext(), new IVolleyListener() {
            @Override
            public void onImageSuccess(Bitmap image) {

            }

            @Override
            public void onSuccessJSONArray(JSONArray response) {
                if(response.length() == 0)
                {
                    SelectedUser.setText("No current requests");
                }
                else
                {
                    for(int i =0; i< response.length(); i++)
                    {
                        try {
                            JSONObject temp = (JSONObject) response.get(i);
                            requestList.add(temp.getString("userOne"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        SelectedUser.setText(requestList.get(0));
                    }
                }

            }



            @Override
            public void onSuccess(JSONObject response) throws JSONException {

            }

            @Override
            public void onSuccess(String response) {

            }

            @Override
            public void onError() {

            }
        }));
        FRLogic.Allrequests();




        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pos < requestList.size()-1)
                {
                    pos++;
                    SelectedUser.setText(requestList.get(pos));
                }
                else
                {

                }

            }
        });

        last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pos > 0) {
                    pos--;
                    SelectedUser.setText(requestList.get(pos));
                }
                else
                {

                }
            }
        });






        Accept.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                TargetUserInformation.username = SelectedUser.getText().toString();
                FRLogic.setModel(new RequestServerForService(getApplicationContext(), new IVolleyListener() {
                    @Override
                    public void onImageSuccess(Bitmap image) {

                    }

                    @Override
                    public void onSuccessJSONArray(JSONArray response) {

                    }

                    @Override
                    public void onSuccess(JSONObject response) throws JSONException {

                    }

                    @Override
                    public void onSuccess(String response) {

                    }

                    @Override
                    public void onError() {

                    }
                }));
                FRLogic.AcceptRequest();
                Intent i = new Intent(FreindRequestsActivity.this, FreindRequestsActivity.class);
                startActivity(i);
            }
        });
        Deny.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view) {
                TargetUserInformation.username = SelectedUser.getText().toString();
                FRLogic.setModel(new RequestServerForService(getApplicationContext(), new IVolleyListener() {
                    @Override
                    public void onImageSuccess(Bitmap image) {

                    }

                    @Override
                    public void onSuccessJSONArray(JSONArray response) {

                    }

                    @Override
                    public void onSuccess(JSONObject response) throws JSONException {

                    }

                    @Override
                    public void onSuccess(String response) {

                    }

                    @Override
                    public void onError() {

                    }
                }));
                FRLogic.DenyRequest();
                Intent i = new Intent(FreindRequestsActivity.this, FreindRequestsActivity.class);
                startActivity(i);
            }

        });

        Back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FreindRequestsActivity.this, FriendListActivity.class);
                startActivity(i);
            }
        });

        refresh.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FreindRequestsActivity.this, FreindRequestsActivity.class);
                startActivity(i);
            }
        });

    }

    //buttons here
}