package sg.edu.np.mad.Sharecipe.ui.main.profile.stats;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.models.Stats;
import sg.edu.np.mad.Sharecipe.models.User;

public class StatsAdapter extends RecyclerView.Adapter<StatsViewHolder> {

    private User user;
    private final List<Stats> statsList;

    public StatsAdapter(User user, @NonNull List<Stats> statsList) {
        this.user = user;
        this.statsList = statsList;
    }

    @NonNull
    @Override
    public StatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_stats, parent, false);
        StatsViewHolder holder = new StatsViewHolder(view);

        // Dynamic on click action
        holder.card.setOnClickListener(v -> {
            if (user == null) {
                // Just in case user have load.
                return;
            }
            holder.action.onClick(holder.itemView.getContext(), user);
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull StatsViewHolder holder, int position) {
        Stats stats = statsList.get(position);
        holder.name.setText(stats.getName());
        holder.number.setText(String.valueOf(stats.getNumber()));
        holder.action = StatsActions.forType(stats.getStatsType());
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

    public void setUser(User user) {
        this.user = user;
    }
}
