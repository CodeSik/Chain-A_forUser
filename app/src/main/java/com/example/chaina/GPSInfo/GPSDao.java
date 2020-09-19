package com.example.chaina.GPSInfo;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface  GPSDao {
    @Query("SELECT * FROM gps_table")
    public GPS[] getAll();

    @Insert
    void insertAll(GPS... gps);

    @Insert
    void insert(GPS gps);

    @Query("DELETE FROM gps_table")
    void deleteAll();
}
