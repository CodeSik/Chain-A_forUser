package GPSTotalInfo;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface GPSTotalDao {
    @Query("SELECT * FROM gps_total_table")
    public GPSTotal[] getAll();

    @Insert
    void insertAll(GPSTotal... gps);

    @Insert
    void insert(GPSTotal gps);

    @Query("DELETE FROM gps_total_table")
    void deleteAll();
}
