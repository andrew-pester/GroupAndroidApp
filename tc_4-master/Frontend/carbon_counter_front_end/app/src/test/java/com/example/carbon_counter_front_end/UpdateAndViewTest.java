package com.example.carbon_counter_front_end;

import android.content.Context;
import android.widget.EditText;

import com.example.carbon_counter_front_end.data.logic.MainActivityLogic;
import com.example.carbon_counter_front_end.data.logic.UpdateStatsLogic;
import com.example.carbon_counter_front_end.data.model.RequestServerForService;
import com.example.carbon_counter_front_end.data.view.UpdateActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UpdateAndViewTest {


    @Mock
    private RequestServerForService model;

    @Mock private MainActivityLogic MockLogi;



    JSONObject mockJSON = mock(JSONObject.class);



    @Before
    public void setup()
    {

        when(MockLogi.Permissions((String)any(String.class)))
                .thenAnswer(x -> {
                    boolean result;
                    if(x.getArgumentAt(0,String.class) == "ADMIN" || x.getArgumentAt(0,String.class) == "DEV")
                    {
                        result = true;
                    }
                    else
                    {
                        result = false;
                    }
                    return result;
                });

    }


    @Test
    public void UpdateUser() throws JSONException {

      //  String temp = "ADMIN";

        UpdateStatsLogic mock = mock(UpdateStatsLogic.class);
        mock.setModel(model);
        mock.authenticate(mockJSON);
        verify(mock).authenticate(mockJSON);



        assertEquals(true,MockLogi.Permissions("ADMIN"));

        assertEquals(false,MockLogi.Permissions("USER"));



    }
}