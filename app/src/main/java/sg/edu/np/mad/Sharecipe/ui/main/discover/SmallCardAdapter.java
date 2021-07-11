package sg.edu.np.mad.Sharecipe.ui.main.discover;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.Sharecipe.R;

public class SmallCardAdapter extends RecyclerView.Adapter<SmallCardViewHolder> {

    public SmallCardAdapter() {
    }

    @NonNull
    @Override
    public SmallCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_discover_small, parent, false);
        return new SmallCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SmallCardViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 6;
    }
}
