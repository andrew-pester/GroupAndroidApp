package com.example.carbon_counter_front_end.data.model;

import android.content.Context;

import androidx.room.Room;

/**
 * Not in service
 * @author Zachary Current
 */
public class InsertDataThread implements Runnable {
    private String password;
    private String username;
    private Context context;

    public InsertDataThread(String username, String password, Context context){
        this.password = password;
        this.username = username;
        this.context = context;
    }

    @Override
    public void run(){
        AppDatabase db = Room.databaseBuilder(context,
                AppDatabase.class, "user-database").build();

        db.userDao().insert(new User(username, password, null));
    }
}
