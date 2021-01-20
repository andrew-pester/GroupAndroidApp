package com.example.carbon_counter_front_end.data.view;


import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.example.carbon_counter_front_end.R;
import com.example.carbon_counter_front_end.data.logic.ViewLogic;
import com.example.carbon_counter_front_end.data.model.IVolleyListener;
import com.example.carbon_counter_front_end.data.model.RequestServerForService;
import com.example.carbon_counter_front_end.data.model.UserInformation;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;

/**
 * View user's stats
 * @author Morgan Funk & Zachary Current & Andrew Pester
 */
public class ViewActivity extends AppCompatActivity {
    private String TAG = ViewActivity.class.getSimpleName();
    private String tag_json_get= "json_obj_get";

    private ArrayList<String> milesDriven = new ArrayList<>();
    private ArrayList<String> waterUsed  = new ArrayList<>();
    private ArrayList<String> powerUsed = new ArrayList<>();
    private ArrayList<String> meatConsumed = new ArrayList<>();
    private ArrayList<String> wasteProduced = new ArrayList<>();
    private ArrayList<String> date = new ArrayList<>();
    private int startYear = 0;
    private int startMonth = 0;
    private int startDay = 0;

    private String username;
    private String password;
    private JSONObject UserInfo = new JSONObject();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ViewLogic viewLogic = new ViewLogic(ViewActivity.this, getApplicationContext());
        setContentView(R.layout.activity_view);
        username = UserInformation.username;


        // getMiles(username);
        viewLogic.setModel(new RequestServerForService(getApplicationContext(), new IVolleyListener() {
            @Override
            public void onImageSuccess(Bitmap image) {

            }

            @Override
            public void onSuccessJSONArray(JSONArray response) {
                LineChart graph = (LineChart) findViewById(R.id.Graph);



                for(int i = 0; i<response.length();i++){
                    try {
                        JSONObject temp = (JSONObject) response.get(i);
                        milesDriven.add(temp.getString("milesDriven"));
                        waterUsed.add(temp.getString("water"));
                        powerUsed.add(temp.getString("power"));
                        meatConsumed.add(temp.getString("meat"));
                        wasteProduced.add(temp.getString("garbage"));
                        date.add(temp.getString("date"));

                        // get start date of data and increment by 2 days then
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
                if(response.length()==0){
                    TextView mDriven = findViewById(R.id.milesDriven);
                    mDriven.setText("No stats available");
                    TextView wUsed = findViewById(R.id.water);
                    wUsed.setText("No stats available");
                    TextView pUsed = findViewById(R.id.power);
                    pUsed.setText("No stats available");
                    TextView wProduced = findViewById(R.id.waste);
                    wProduced.setText("No stats available");
                    TextView mConsumed = findViewById(R.id.meat);
                    mConsumed.setText("No stats available");
                }else {
                    TextView mDriven = findViewById(R.id.milesDriven);
                    mDriven.setText(milesDriven.get(milesDriven.size()-1));
                    TextView wUsed = findViewById(R.id.water);
                    wUsed.setText(waterUsed.get(waterUsed.size()-1));
                    TextView pUsed = findViewById(R.id.power);
                    pUsed.setText(powerUsed.get(powerUsed.size()-1));
                    TextView wProduced = findViewById(R.id.waste);
                    wProduced.setText(wasteProduced.get(wasteProduced.size()-1));
                    TextView mConsumed = findViewById(R.id.meat);
                    mConsumed.setText(meatConsumed.get(meatConsumed.size()-1));
                    ArrayList<Entry> milesEnt = new ArrayList<>();
                    ArrayList<Entry> waterEnt = new ArrayList<>();
                    ArrayList<Entry> powerEnt = new ArrayList<>();
                    ArrayList<Entry> meatEnt = new ArrayList<>();
                    ArrayList<Entry> wasteEnt = new ArrayList<>();
                    double totalMiles = 0;
                    double totalPower = 0;
                    for(int i =0; i<milesDriven.size(); i++){
                        //need to set date here most likely
                        totalMiles += Double.parseDouble(milesDriven.get(i));
                        totalPower += Double.parseDouble(powerUsed.get(i));
                        milesEnt.add(new Entry(i, Float.parseFloat(milesDriven.get(i))));
                        waterEnt.add(new Entry(i, Float.parseFloat(waterUsed.get(i))));
                        powerEnt.add(new Entry(i, Float.parseFloat(powerUsed.get(i))));
                        meatEnt.add(new Entry(i, Float.parseFloat(meatConsumed.get(i))));
                        wasteEnt.add(new Entry(i, Float.parseFloat(wasteProduced.get(i))));
                    }
                    totalMiles *=.9061;
                    totalPower *=0.99;
                    TextView CO2 = findViewById(R.id.CO2);
                    CO2.setText(totalMiles+totalPower+" lbs");

                    ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();
                    LineDataSet miles = new LineDataSet(milesEnt, "Miles Driven");
                    miles.setDrawCircles(true);
                    miles.setColor(Color.BLACK);
                    LineDataSet water = new LineDataSet(waterEnt, "Water Used");
                    water.setDrawCircles(true);
                    water.setColor(Color.BLUE);
                    LineDataSet power = new LineDataSet(powerEnt, "Power Used");
                    power.setDrawCircles(true);
                    power.setColor(Color.YELLOW);
                    LineDataSet waste = new LineDataSet(wasteEnt, "Garbage");
                    waste.setDrawCircles(true);
                    waste.setColor(Color.GREEN);
                    LineDataSet meat = new LineDataSet(meatEnt, "Meat Consumed");
                    meat.setDrawCircles(true);
                    meat.setColor(Color.RED);

                    lineDataSets.add(miles);
                    lineDataSets.add(water);
                    lineDataSets.add(power);
                    lineDataSets.add(waste);
                    lineDataSets.add(meat);
                    //sets x axis points
                    /*
                    startYear = Integer.parseInt(date.get(0).substring(0,4));
                    startMonth = Integer.parseInt(date.get(0).substring(5,7));
                    startDay = Integer.parseInt(date.get(0).substring(8,10));
                    Calendar myCal = new GregorianCalendar(startYear,startMonth,startDay);
                     */
                    graph.getXAxis().setLabelCount(milesDriven.size(),true);
                    graph.getXAxis().setValueFormatter(new MyValueFormatter());
                    graph.getXAxis().setLabelRotationAngle(90f);
                    graph.getAxisRight().setDrawLabels(false);
                    graph.setData(new LineData(lineDataSets));
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

        viewLogic.getMonthlyStats();


    }
    public class MyValueFormatter extends ValueFormatter {

        @Override
        public String getFormattedValue(float value) {
            return date.get((int) value % date.size());
        }
    }
}
