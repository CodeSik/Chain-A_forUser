package GPSTotalInfo;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "gps_total_table")
public class GPSTotal {

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @PrimaryKey
    @NonNull
    @ColumnInfo(name="gps")
    private double longitude; //경도
    private double latitude; // 위도
    private String time; // 시간

    public GPSTotal() {
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getTime() {
        return time;
    }



    public GPSTotal(double longitude, double latitude, String time)
    {
        this.latitude=latitude;
        this.longitude=longitude;
        this.time = time;
    }
}
