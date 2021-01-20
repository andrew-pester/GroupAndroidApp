package com.example.carbon_counter_front_end.data.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.carbon_counter_front_end.R;
import com.example.carbon_counter_front_end.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Registration page
 * @author Morgan Funk
 */
public class CreateUserActivity extends AppCompatActivity {
//    EditText Username;
//    EditText email;
//    EditText password;


    //EditText passwordCheck;

    private String TAG = CreateUserActivity.class.getSimpleName();
    private String tag_json_POST= "json_obj_POST";
    RequestQueue requestQueue;




    public CreateUserActivity() throws IOException {
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        final TextView Username = (TextView) findViewById(R.id.et_name);
        final TextView password = (TextView) findViewById(R.id.et_password);
        final TextView email = (TextView) findViewById(R.id.et_email);
        Button createAccount = (Button) findViewById(R.id.btn_register);;




        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // Toast.makeText(CreateUserActivity.this, Username.getText().toString(), LENGTH_SHORT).show();

              //  Toast.makeText(CreateUserActivity.this, email.getText().toString(), LENGTH_SHORT).show();

               // Toast.makeText(CreateUserActivity.this, password.getText().toString(), LENGTH_SHORT).show();
                try {
                    PostUser(Username.getText().toString(),email.getText().toString(),password.getText().toString());
                    Intent i = new Intent(CreateUserActivity.this, LoginActivity.class);
                    startActivity(i);
                  //  Toast.makeText(CreateUserActivity.this, "in the request", LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Toast.makeText(CreateUserActivity.this, "after the request", LENGTH_SHORT).show();
                // checkDataEntered();
            }
        });

    }

    boolean verifyEmail(EditText text)
    {
        CharSequence email = text.getText().toString().trim();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    boolean isEmpty(EditText text)
    {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

//    void checkDataEntered() {
//        if (isEmpty(Username))
//        {
//            Toast t = Toast.makeText(this, "Error: Please enter a Name to register", Toast.LENGTH_SHORT);
//            t.show();
//        }
//
//        if(verifyEmail(email) == false)
//        {
//            email.setError("Please enter a valid Email");
//        }
//
//        if(isEmpty(password))
//        {
//            Toast t = Toast.makeText(this, "Error: Please enter a password", Toast.LENGTH_SHORT);
//            t.show();
//
//        }
////        if(isEmpty(passwordCheck)) {
////            Toast t = Toast.makeText(this, "Error: Please enter the same password as above", Toast.LENGTH_SHORT);
////            t.show();
////
////        }
////        if( !password.toString().equals(passwordCheck.toString()) )
////        {
////            Toast t = Toast.makeText(this, "Error: Passwords must match", Toast.LENGTH_SHORT);
////            t.show();
////        }

//    }

    private void PostUser(String user, String email, String pass) throws JSONException {
        String url = "http://10.24.227.38:8080/user/add";

        //url += "/" + username;
        final JSONObject jsonParam = new JSONObject();
        jsonParam.put("username", user);
        jsonParam.put("email", email);
        jsonParam.put("password", pass);
        jsonParam.put("role", "USER");
     //   Toast.makeText(CreateUserActivity.this, jsonParam.toString(), Toast.LENGTH_LONG).show(); For Debugging later


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, jsonParam, // IF YOU WANT TO SEND A JSONOBJECT WITH POST THEN PASS IT HERE
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        System.out.println(response);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_POST);
    }

}