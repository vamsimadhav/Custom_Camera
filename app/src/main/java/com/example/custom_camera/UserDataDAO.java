package com.example.custom_camera;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;


@Dao
public interface UserDataDAO {

    @Query("select * from userData")
    List<UserData> getUserData();

    @Insert
    void addUserData(UserData userData);

    @Update
    void updateUserData(UserData userData);

    @Delete
    void deleteUserData(UserData userData);
}
