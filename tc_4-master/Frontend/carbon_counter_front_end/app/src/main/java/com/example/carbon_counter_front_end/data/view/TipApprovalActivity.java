package com.example.carbon_counter_front_end.data.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carbon_counter_front_end.R;
import com.example.carbon_counter_front_end.data.logic.TipApprovalLogic;
import com.example.carbon_counter_front_end.data.model.IVolleyListener;
import com.example.carbon_counter_front_end.data.model.RequestServerForService;

import org.json.JSONArray;
import org.json.JSONObject;

public class TipApprovalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip_approval);


        final TextView subject = (TextView) findViewById(R.id.textViewSubjectApprove);
        final TextView description = (TextView) findViewById(R.id.textViewDescriptionApprove);
        Button next = (Button) findViewById(R.id.buttonNextApprove);
        Button prev = (Button) findViewById(R.id.buttonPrevApprove);
        Button approve = (Button) findViewById(R.id.buttonApprove);
        Button disapprove = (Button) findViewById(R.id.buttonDisapprove);
        Button back = (Button) findViewById(R.id.buttonApprovalBack);



        final TipApprovalLogic tipApprovalLogic = new TipApprovalLogic(this, getApplicationContext());
        tipApprovalLogic.setModel(new RequestServerForService(getApplicationContext(), new IVolleyListener() {
            @Override
            public void onImageSuccess(Bitmap image) {

            }

            @Override
            public void onSuccessJSONArray(JSONArray response) {
                System.out.println(response);
                tipApprovalLogic.setTips(response);
                String subjectInfo = tipApprovalLogic.getSubject();
                String descriptionInfo = tipApprovalLogic.getDescription();

                subject.setText(subjectInfo);
                description.setText(descriptionInfo);
            }

            @Override
            public void onSuccess(JSONObject response) {

            }

            @Override
            public void onSuccess(String response) {
                Intent i = new Intent(TipApprovalActivity.this, TipApprovalActivity.class);
                startActivity(i);
            }

            @Override
            public void onError() {

            }
        }));

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Call logic for the tip to be displayed
                tipApprovalLogic.setNextTip();
                String subjectInfo = tipApprovalLogic.getSubject();
                String descriptionInfo = tipApprovalLogic.getDescription();

                subject.setText(subjectInfo);
                description.setText(descriptionInfo);
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Call logic for the tip to be displayed
                tipApprovalLogic.setPrevTip();
                String subjectInfo = tipApprovalLogic.getSubject();
                String descriptionInfo = tipApprovalLogic.getDescription();

                subject.setText(subjectInfo);
                description.setText(descriptionInfo);
            }
        });

        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tipApprovalLogic.approveTip();
            }
        });

        disapprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tipApprovalLogic.deleteTip();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TipApprovalActivity.this, AdminOverview.class);
                startActivity(i);
            }
        });

        tipApprovalLogic.getTips();

    }
}