package com.example.carbon_counter_front_end.data.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.Telephony;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carbon_counter_front_end.R;
import com.example.carbon_counter_front_end.data.logic.LoginLogic;
import com.example.carbon_counter_front_end.data.model.IVolleyListener;
import com.example.carbon_counter_front_end.data.model.RequestServerForService;
import com.example.carbon_counter_front_end.data.model.UserInformation;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;


/**
 * Login Screen for Carbon Counter
 * @author Zachary Current
 */
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        final LoginLogic loginLogic = new LoginLogic(LoginActivity.this, getApplicationContext());
        final TextView failedLogin = (TextView) findViewById(R.id.failedLogin);
        final TextView failedLogin2 = (TextView) findViewById(R.id.failedLogin2);
        final TextView username = (TextView) findViewById(R.id.username);
        final TextView password = (TextView) findViewById(R.id.password);
        Button loginButton = (Button) findViewById(R.id.buttonLogin);
        Button registerButton = (Button) findViewById(R.id.buttonRegister);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*InsertDataThread insertDataThread = new InsertDataThread(username.getText().toString(), password.getText().toString(), getApplicationContext());

                Thread insertData = new Thread(insertDataThread);

                insertData.start();*/

                loginLogic.setModel(new RequestServerForService(getApplicationContext(), new IVolleyListener() {
                    @Override
                    public void onImageSuccess(Bitmap image) {

                    }

                    @Override
                    public void onSuccessJSONArray(JSONArray response) {
                        //do nothing
                    }

                    @Override
                    public void onSuccess(JSONObject response) {
                        try {
                            UserInformation.role = response.getString("role");
                            UserInformation.username = response.getString("username");

                            Draft[] drafts = {new Draft_6455()};

                            try {
                                UserInformation.ws = new WebSocketClient(new URI("ws://coms-309-tc-04.cs.iastate.edu:8080/notify/" + UserInformation.username), (Draft) drafts[0]) {


                                    @Override
                                    public void onOpen(ServerHandshake serverHandshake) {

                                    }

                                    @Override
                                    public void onMessage(String s) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                                            }
                                        });
                                        System.out.println(s);
                                    }

                                    @Override
                                    public void onClose(int i, String s, boolean b) {

                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        System.out.println(e.getMessage());
                                    }
                                };
                            } catch (Exception e){
                                System.out.println(e.getMessage());
                            }

                            UserInformation.ws.connect();



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        loginLogic.clearError(failedLogin, failedLogin2);
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(i);



                    }

                    @Override
                    public void onSuccess(String response) {

                    }

                    @Override
                    public void onError() {
                        loginLogic.displayError(failedLogin, failedLogin2);
                    }
                }));

                loginLogic.authenticate(username.getText().toString(), password.getText().toString());
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, CreateUserActivity.class);

                startActivity(i);
            }
        });
    }




}

