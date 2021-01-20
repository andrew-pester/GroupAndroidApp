package com.example.carbon_counter_front_end.data.model;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

/**
 * Not in service
 * @author Zachary Current
 */
public class UserRepository {
    private UserDao mUserDao;
    private LiveData<List<User>> mAllUsers;

    public UserRepository(Application application){
        AppDatabase db = AppDatabase.getDatabase(application);
        mUserDao = db.userDao();
        mAllUsers = mUserDao.getAll();
    }

    public LiveData<List<User>> getAllUsers() {
        return  mAllUsers;
    }

    void insert(User user){

    }
}
