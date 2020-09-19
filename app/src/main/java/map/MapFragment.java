package map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.chaina.R;

import net.daum.mf.map.api.MapView;

public class MapFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        // java code;
        MapView mapView = new MapView(v.getContext());


        ViewGroup mapViewContainer = (ViewGroup) v.findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);
        return v;
    }
}