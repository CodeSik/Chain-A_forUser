package GPSTotalInfo;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import GPSInfo.GPS;

@Dao
public interface GPSTotalDao {
    @Query("SELECT * FROM gps_table")
    public GPS[] getAll();

    @Insert
    void insertAll(GPS... gps);

    @Insert
    void insert(GPS gps);

    @Query("DELETE FROM gps_table")
    void deleteAll();
}
