package GPSTotalInfo;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {GPSTotal.class}, version =  1)
public abstract class GPSTotalDatabase extends RoomDatabase {

    //데이터베이스를 매번 생성하는건 리소스를 많이사용하므로 싱글톤이 권장된다고한다.
    private static GPSTotalDatabase INSTANCE;

    public abstract GPSTotalDao gpsTotalDao();


    //디비객체생성 가져오기
    public static GPSTotalDatabase getAppDatabase(Context context){
        if(INSTANCE == null){
            synchronized (GPSTotalDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            GPSTotalDatabase.class, "gps_total_database")
                            .build();
                }
            }
        }
        return  INSTANCE;
    }

    //디비객체제거
    public static void destroyInstance() {
        INSTANCE = null;
    }

}
