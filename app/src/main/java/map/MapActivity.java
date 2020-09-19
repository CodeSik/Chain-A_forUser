package map;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.chaina.R;

import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        MapView mapView = new MapView(this);
        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

        MapPolyline polyline = new MapPolyline();
        polyline.setTag(3000);
        polyline.setLineColor(Color.argb(128, 0, 51, 196));

        polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.537229, 127.005515));
        polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.545024,127.03923));
        polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.527896,127.036245));
        polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.541889,127.095388));

        mapView.addPolyline(polyline);

        MapPointBounds mapPointBounds = new MapPointBounds(polyline.getMapPoints());
        int padding = 100; // px
        mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));

        Request.getService().get().enqueue(new Callback<ArrayList<RecordResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<RecordResponse>> call, Response<ArrayList<RecordResponse>> response) {
                Log.d("RESPONSE", response.body().toString());
                Toast.makeText(MapActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ArrayList<RecordResponse>> call, Throwable t) {
                Log.e("RESPONSE", t.toString());
                Toast.makeText(MapActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}