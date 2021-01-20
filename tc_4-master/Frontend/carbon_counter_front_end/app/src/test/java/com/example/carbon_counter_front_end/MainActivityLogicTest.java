package com.example.carbon_counter_front_end;

import android.content.Context;

import com.example.carbon_counter_front_end.data.logic.MainActivityLogic;
import com.example.carbon_counter_front_end.data.model.RequestServerForService;
import com.example.carbon_counter_front_end.data.view.MainActivity;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class MainActivityLogicTest {
    @Mock
    private Context context;

    @Mock
    private MainActivity mainActivity;

    @Mock
    private RequestServerForService model;



    @Test
    public void testSetNextImage(){
        MainActivityLogic mainActivityLogic = new MainActivityLogic(mainActivity, context);
        mainActivityLogic.setModel(model);
        JSONObject toAdd = new JSONObject();
        mainActivityLogic.addNews(toAdd);
        mainActivityLogic.addNews(toAdd);
        mainActivityLogic.addNews(toAdd);
        mainActivityLogic.addNews(toAdd);


        assertEquals(0, mainActivityLogic.getNewsIndex());
        mainActivityLogic.setNextImage();
        assertEquals(1, mainActivityLogic.getNewsIndex());
        mainActivityLogic.setNextImage();
        mainActivityLogic.setNextImage();
        assertEquals(3, mainActivityLogic.getNewsIndex());
    }

    @Test
    public void testSetPrevImage() {
        MainActivityLogic mainActivityLogic = new MainActivityLogic(mainActivity, context);
        mainActivityLogic.setModel(model);

        JSONObject toAdd = new JSONObject();
        mainActivityLogic.addNews(toAdd);
        mainActivityLogic.addNews(toAdd);
        mainActivityLogic.addNews(toAdd);
        mainActivityLogic.addNews(toAdd);

        assertEquals(0, mainActivityLogic.getNewsIndex());
        mainActivityLogic.setPrevImage();
        assertEquals(3, mainActivityLogic.getNewsIndex());
        mainActivityLogic.setPrevImage();
        assertEquals(2, mainActivityLogic.getNewsIndex());
    }
}
