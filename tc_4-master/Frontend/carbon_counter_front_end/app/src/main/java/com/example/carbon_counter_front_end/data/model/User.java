package com.example.carbon_counter_front_end.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
/**
 * Not in service
 * @author Zachary Current
 */
@Entity
public class User {
    @PrimaryKey @NonNull
    public String username;


    @ColumnInfo(name = "password")
    public String password;

    @ColumnInfo(name = "role")
    public String role;

    public User(String username, String password, String role){
        this.username = username;
        this.password = password;
        this.role = role;
    }

}
