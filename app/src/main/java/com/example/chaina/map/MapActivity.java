package com.example.chaina.map;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chaina.R;
import com.example.chaina.map.gara.Data;
import com.example.chaina.map.gara.Trace;

import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPOIItem;
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

        ArrayList<MapPOIItem> markers = new ArrayList<>();
        MapPolyline myLine = new MapPolyline();
        MapPolyline sickLine = new MapPolyline();

        Data.getScene1().forEach(person -> {
            MapPOIItem.MarkerType type;
            MapPolyline polyline;
            int lineColor, markerColor;
            if(person.getState() == 0) {
                polyline = myLine;
                type = MapPOIItem.MarkerType.BluePin;
                lineColor = Color.argb(128, 0, 51, 196);
                markerColor = Color.argb(64, 0, 196, 51);
            }
            else {
                polyline = sickLine;
                type = MapPOIItem.MarkerType.RedPin;
                lineColor = Color.argb(128, 196, 51, 21);
                markerColor = Color.argb(64, 196, 31, 0);
            }

            for (Trace trace : person.getTraces()) {
                if(!trace.getCompanyName().isEmpty()) {
                    MapPOIItem marker = new MapPOIItem();
                    marker.setItemName(trace.getCompanyName());
                    marker.setMapPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(trace.getLat()), Double.parseDouble(trace.getLng())));
                    marker.setMarkerType(type);
                    markers.add(marker);
                }

                polyline.setLineColor(lineColor);
                polyline.addPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(trace.getLat()), Double.parseDouble(trace.getLng())));
            }
        });

        mapView.addPolyline(myLine);
        mapView.addPolyline(sickLine);
        for (MapPOIItem marker : markers) {
            mapView.addPOIItem(marker);
        }

        MapPointBounds mapPointBounds = new MapPointBounds(myLine.getMapPoints());
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