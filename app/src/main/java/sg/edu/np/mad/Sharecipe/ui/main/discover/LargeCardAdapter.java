package sg.edu.np.mad.Sharecipe.ui.main.discover;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.Sharecipe.R;

public class LargeCardAdapter extends RecyclerView.Adapter<LargeCardViewHolder> {

    public LargeCardAdapter() {
    }

    @NonNull
    @Override
    public LargeCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_discover_large, parent, false);
        return new LargeCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LargeCardViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
