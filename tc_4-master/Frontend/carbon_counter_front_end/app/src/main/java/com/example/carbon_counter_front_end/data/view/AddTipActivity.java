package com.example.carbon_counter_front_end.data.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carbon_counter_front_end.R;
import com.example.carbon_counter_front_end.data.logic.AddTipLogic;
import com.example.carbon_counter_front_end.data.model.IVolleyListener;
import com.example.carbon_counter_front_end.data.model.RequestServerForService;
import com.example.carbon_counter_front_end.data.model.UserInformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Activity to add a tip to the server
 * @author Zachary Current
 */
public class AddTipActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tip);



        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        Button submit = (Button) findViewById(R.id.buttonSubmitTip);
        Button back = (Button) findViewById(R.id.buttonBackTip);
        final EditText subject = (EditText) findViewById(R.id.editTextSubject);
        final EditText description = (EditText) findViewById(R.id.editTextDescription);

        final AddTipLogic addTipLogic = new AddTipLogic(this, getApplicationContext());
        addTipLogic.setModel(new RequestServerForService(getApplicationContext(), new IVolleyListener() {

            @Override
            public void onImageSuccess(Bitmap image) {

            }

            @Override
            public void onSuccessJSONArray(JSONArray response) {

            }

            @Override
            public void onSuccess(JSONObject response) throws JSONException {
                UserInformation.ws.send("@ADMIN " + subject.getText().toString() + " has been added for approval!");
                Intent i = new Intent(AddTipActivity.this, MainActivity.class);
                startActivity(i);
            }

            @Override
            public void onSuccess(String response) {

            }

            @Override
            public void onError() {

            }
        }));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AddTipActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String category = "";
                if(spinner.getSelectedItem().toString().equals("Emissions")){
                    category = "CARBON";
                } else if(spinner.getSelectedItem().toString().equals("Water")){
                    category = "WATER";
                } else if(spinner.getSelectedItem().toString().equals("Energy")){
                    category = "ENERGY";
                } else {
                    category = "GARBAGE";
                }
                addTipLogic.addTip(category, description.getText().toString(), subject.getText().toString());
            }
        });
    }
}