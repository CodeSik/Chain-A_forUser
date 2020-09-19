package map;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chaina.MainActivity;
import com.example.chaina.R;

import java.util.ArrayList;

public class TrafficrecordAdapter extends RecyclerView.Adapter<TrafficrecordAdapter.ViewHolder> {
    private ArrayList<Trafficrecord> items;
    public TrafficrecordAdapter(ArrayList<Trafficrecord> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trafficrecord, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Trafficrecord item = items.get(position);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.v.getContext(), MapActivity.class);
                holder.v.getContext().startActivity(intent);
            }
        };

        holder.bind(listener, item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View v;
        TextView tvDate, tvState;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            v = itemView;
            tvDate = v.findViewById(R.id.tvDate);
            tvState = v.findViewById(R.id.tvState);
        }

        void bind(View.OnClickListener listener, Trafficrecord item) {
            this.tvDate.setText(item.date);
            this.tvState.setText(item.state);
            if(item.state.equals("안전")) {
                this.tvState.setTextColor(v.getContext().getColor(R.color.colorSafeText));
                this.tvState.setBackgroundResource(R.drawable.bg_safe);
            }
            else {
                this.tvState.setTextColor(v.getContext().getColor(R.color.colorDangerText));
                this.tvState.setBackgroundResource(R.drawable.bg_danger);
            }
            v.setOnClickListener(listener);
        }
    }
}
