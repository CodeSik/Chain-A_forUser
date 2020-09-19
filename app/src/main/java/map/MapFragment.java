package map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chaina.MainActivity;
import com.example.chaina.R;

import net.daum.mf.map.api.MapView;

import java.util.ArrayList;

public class MapFragment extends Fragment {
    private RecyclerView rcvTrafficrecord;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        rcvTrafficrecord = v.findViewById(R.id.rcvTrafficrecord);

        ArrayList<Trafficrecord> items = new ArrayList<>();
        items.add(new Trafficrecord("2020.09.18 (금)", "위험"));
        items.add(new Trafficrecord("2020.09.19 (토)", "안전"));
        items.add(new Trafficrecord("2020.09.20 (일)", "안전"));
        TrafficrecordAdapter adapter = new TrafficrecordAdapter(items);
        rcvTrafficrecord.setAdapter(adapter);

        return v;
    }
}