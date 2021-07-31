package sg.edu.np.mad.Sharecipe.ui.main.profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.models.Stats;

public class StatsAdapter extends RecyclerView.Adapter<StatsViewHolder> {

    private final List<Stats> statsList;

    public StatsAdapter(@NonNull List<Stats> statsList) {
        this.statsList = statsList;
    }

    @NonNull
    @Override
    public StatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_stats, parent, false);
        return new StatsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatsViewHolder holder, int position) {
        Stats stats = statsList.get(position);
        holder.name.setText(stats.getName());
        holder.number.setText(String.valueOf(stats.getNumber()));
    }

    @Override
    public int getItemCount() {
        return statsList.size();
    }

    public void setStatsList(List<Stats> statsList) {
        this.statsList.clear();
        this.statsList.addAll(statsList);
        notifyDataSetChanged();
    }
}
