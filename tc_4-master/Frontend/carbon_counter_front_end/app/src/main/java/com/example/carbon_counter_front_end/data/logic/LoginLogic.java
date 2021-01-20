package com.example.carbon_counter_front_end.data.logic;

import android.content.Context;
import android.widget.TextView;

import com.example.carbon_counter_front_end.data.model.RequestServerForService;
import com.example.carbon_counter_front_end.data.model.UserInformation;
import com.example.carbon_counter_front_end.data.view.LoginActivity;

/**
 * Logic class to take care of the logic for loginActivity
 * @author Zachary Current
 */
public class LoginLogic {
    private RequestServerForService model;
    private LoginActivity view;
    private Context context;

    /**
     * LoginLogic Constructor - To take care of logic for the loginActivity
     * @param view view of loginActivity
     * @param context context of loginActivity
     */
    public LoginLogic(LoginActivity view, Context context){
        this.view = view;
        this.context = context;
    }

    /**
     * Set loginLogic model
     * @param m model to be set of type RequestServerForService
     */
    public void setModel(RequestServerForService m) { this.model = m;}

    /**
     * Method to contact server and verify the user's credentials
     * @param username Username entered in loginActivity
     * @param password Password entered in loginActivity
     */
    public void authenticate(String username, String password) {
        UserInformation.username = username;
        UserInformation.password = password;

        String url = "http://10.24.227.38:8080/user";
        url += "/" + username;
        System.out.println(url);

        model.contactServer(url);
    }

    /**
     * When authentication passes, clear any previous failed texts
     * @param failedLogin failedLogin field 1 from loginActivity
     * @param failedLogin2 failedLogin field 2 from loginActivity
     */
    public void clearError(TextView failedLogin, TextView failedLogin2) {
        failedLogin.setText("");
        failedLogin2.setText("");
    }

    /**
     * When authentication fails, display the error on loginActivity
     * @param failedLogin failedLogin field 1 from loginActivity
     * @param failedLogin2 failedLogin field 2 from loginActivity
     */
    public void displayError(TextView failedLogin, TextView failedLogin2) {
        failedLogin.setText("Invalid username or password!");
        failedLogin2.setText("Please register or try again!");
    }
}
