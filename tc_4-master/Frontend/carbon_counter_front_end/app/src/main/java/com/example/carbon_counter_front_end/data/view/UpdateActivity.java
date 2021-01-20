package com.example.carbon_counter_front_end.data.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.example.carbon_counter_front_end.R;
import com.example.carbon_counter_front_end.data.logic.UpdateStatsLogic;
import com.example.carbon_counter_front_end.data.model.IVolleyListener;
import com.example.carbon_counter_front_end.data.model.RequestServerForService;
import com.example.carbon_counter_front_end.data.model.User;
import com.example.carbon_counter_front_end.data.model.UserInformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static java.lang.Double.parseDouble;

/**
 * Update Stats page
 * @author Morgan Funk
 */
public class UpdateActivity extends AppCompatActivity {

    private String TAG = UpdateActivity.class.getSimpleName();
    private String tag_json_POST= "json_obj_POST";
    private String username;
    private String password;
    private LiveData<List<User>> myUser;
    private String miles;
    private String water;
    private String power;
    private String meat;
    private String waste;
    final JSONObject userUpdate = new JSONObject();



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);


/*
        UserRepository mUserRepository = new UserRepository(getApplication());
        myUser = mUserRepository.getAllUsers();

        username = myUser.getValue().get(0).username;
        password = myUser.getValue().get(0).password;
*/
        username = UserInformation.username;
        password = UserInformation.password;

        Button backButton = (Button) findViewById(R.id.buttonBack);
        Button viewButton = (Button) findViewById(R.id.buttonView2);
        Button submitButton = (Button) findViewById(R.id.buttonSubmit);
        final EditText milesPerWeek = (EditText) findViewById(R.id.milesPerWeek);
        final EditText waterPerWeek = (EditText) findViewById(R.id.waterPerWeek);
        final EditText meatPerWeek = (EditText) findViewById(R.id.meatPerWeek);
        final EditText powerPerWeek = (EditText) findViewById(R.id.powerPerWeek);
        final EditText wastePerWeek = (EditText) findViewById(R.id.wastePerWeek);





        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UpdateActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UpdateActivity.this, ViewActivity.class);
                i.putExtra("username",username);
                startActivity(i);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final UpdateStatsLogic updateStatsLogic = new UpdateStatsLogic(UpdateActivity.this, getApplicationContext());
                try {
                    miles = milesPerWeek.getText().toString();
                    water = waterPerWeek.getText().toString();
                    meat = meatPerWeek.getText().toString();
                    power = powerPerWeek.getText().toString();
                    waste = wastePerWeek.getText().toString();


                    updateInfo();

                    updateStatsLogic.setModel(new RequestServerForService(getApplicationContext(), new IVolleyListener() {
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
                   // Toast.makeText(UpdateActivity.this, (CharSequence) userUpdate, LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                updateStatsLogic.authenticate(userUpdate);
            }

        });



    }

    private void updateInfo() throws JSONException {
//            double milesDriven = parseDouble(miles);
//            double waterUsed = parseDouble(water);

            //String url = "http://10.24.227.38:8080/stats/addDaily";

            //url += "/" + username;
//        "water": "test6@iastate.edu"
//        "power": "test123"
//        "milesDriven": 55.6
//        "meat":
//        "garbage": 34.6 (double)

           // final JSONObject userUpdate = new JSONObject();
        double updateWater = parseDouble(water);
        double updatePower = parseDouble(power);
        double updateMiles = parseDouble(miles);
        double updateWaste = parseDouble(waste);
        double updateMeat = parseDouble(meat);

        userUpdate.put("username", username);
        userUpdate.put("water", updateWater);
        userUpdate.put("power", updatePower);
        userUpdate.put("milesDriven", updateMiles);
        userUpdate.put("meat", updateMeat);
        userUpdate.put("garbage", updateWaste);




    }


}