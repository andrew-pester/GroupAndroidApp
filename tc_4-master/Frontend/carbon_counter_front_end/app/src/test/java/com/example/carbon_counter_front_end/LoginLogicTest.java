package com.example.carbon_counter_front_end;

import android.content.Context;

import com.example.carbon_counter_front_end.data.logic.LoginLogic;
import com.example.carbon_counter_front_end.data.model.RequestServerForService;
import com.example.carbon_counter_front_end.data.model.UserInformation;
import com.example.carbon_counter_front_end.data.view.LoginActivity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class LoginLogicTest {
    @Mock
    private Context context;

    @Mock
    private LoginActivity loginActivity;

    @Mock
    private RequestServerForService model;

    @Test
    public void testStoredUserInformation(){
        LoginLogic loginLogic = new LoginLogic(loginActivity, context);
        loginLogic.setModel(model);

        loginLogic.authenticate("test user", "password");

        assertEquals("test user", UserInformation.username);
        assertEquals("password", UserInformation.password);
    }

    @Test
    public void testChangingStoredUserInformation(){
        LoginLogic loginLogic = new LoginLogic(loginActivity, context);
        loginLogic.setModel(model);

        loginLogic.authenticate("test user", "password");

        assertEquals("test user", UserInformation.username);
        assertEquals("password", UserInformation.password);

        loginLogic.authenticate("zcurrent", "this is my password yo");

        assertEquals("zcurrent", UserInformation.username);
        assertEquals("this is my password yo", UserInformation.password);
    }
}
