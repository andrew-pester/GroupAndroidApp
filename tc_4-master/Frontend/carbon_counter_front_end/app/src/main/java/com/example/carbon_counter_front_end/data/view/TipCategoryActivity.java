package com.example.carbon_counter_front_end.data.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carbon_counter_front_end.R;
import com.example.carbon_counter_front_end.data.logic.TipCategoryLogic;
import com.example.carbon_counter_front_end.data.model.IVolleyListener;
import com.example.carbon_counter_front_end.data.model.RequestServerForService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Stack;

/**
 * Tip Category page - Displays recommended categories based on user's stats.
 * @author Zachary Current
 */
public class TipCategoryActivity extends AppCompatActivity {
    private String TAG = ViewActivity.class.getSimpleName();
    private String tag_json_get = "json_obj_get";
    private String username;
    private String password;

    private final String emissions = "CARBON";
    private final String water = "WATER";
    private final String waste = "GARBAGE";
    private final String energy = "ENERGY";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip_category);



        final TextView recommended = (TextView) findViewById(R.id.textViewRecommended);
        final TextView all = (TextView) findViewById(R.id.textViewAll);
        final Button viewEmissions = (Button) findViewById(R.id.buttonEmissions);
        final Button viewWater = (Button) findViewById(R.id.buttonWater);
        final Button viewWaste = (Button) findViewById(R.id.buttonWaste);
        final Button viewEnergy = (Button) findViewById(R.id.buttonEnergy);
        Button back = (Button) findViewById(R.id.buttonTipCatBack);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TipCategoryActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        viewEmissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TipCategoryActivity.this, ViewCategoryResults.class);
                i.putExtra("category", emissions);
                startActivity(i);
            }
        });

        viewWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TipCategoryActivity.this, ViewCategoryResults.class);
                i.putExtra("category", water);
                startActivity(i);
            }
        });

        viewWaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TipCategoryActivity.this, ViewCategoryResults.class);
                i.putExtra("category", waste);
                startActivity(i);
            }
        });

        viewEnergy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TipCategoryActivity.this, ViewCategoryResults.class);
                i.putExtra("category", energy);
                startActivity(i);
            }
        });

        final TipCategoryLogic tipCategoryLogic = new TipCategoryLogic(this, getApplicationContext());
        tipCategoryLogic.setModel(new RequestServerForService(getApplicationContext(), new IVolleyListener() {
            @Override
            public void onImageSuccess(Bitmap image) {

            }

            @Override
            public void onSuccessJSONArray(JSONArray response) {

            }

            @Override
            public void onSuccess(JSONObject response) {
                Stack<Character> allStack = tipCategoryLogic.setLayout(response);

                Button last = null;
                int marginTop = 230;
                int allStackSize = allStack.size();

                for(int i = 0; i < allStackSize; i++){
                    if(last != null){
                        marginTop = last.getTop() + 200;
                    }
                    char toCompare = allStack.pop();
                    System.out.println(toCompare);
                    switch (toCompare){
                        case 'm':
                            viewEmissions.setTop(marginTop);
                            viewEmissions.setBottom(viewEmissions.getTop() + 175);
                            last = viewEmissions;
                            break;
                        case 'w':
                            viewWater.setTop(marginTop);
                            viewWater.setBottom(viewWater.getTop() + 175);
                            last = viewWater;
                            break;
                        case 'p':
                            viewEnergy.setTop(marginTop);
                            viewEnergy.setBottom(viewEnergy.getTop() + 175);
                            last = viewEnergy;
                            break;
                        case 'g':
                            viewWaste.setTop(marginTop);
                            viewWaste.setBottom(viewWaste.getTop() + 175);
                            last = viewWaste;
                            break;
                        case 'a':
                            if(i == 0){
                                recommended.setVisibility(View.GONE);
                                last = null;
                                marginTop = 230;
                            } else {
                                all.setY(marginTop);
                                last = null;
                                marginTop += 110;
                            }

                            break;
                        default:
                            break;
                    }
                }
            }

            @Override
            public void onSuccess(String response) {

            }

            @Override
            public void onError() {

            }
        }));

        tipCategoryLogic.contactServer();


    }
}