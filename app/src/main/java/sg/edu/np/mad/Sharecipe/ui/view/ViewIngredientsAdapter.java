package sg.edu.np.mad.Sharecipe.ui.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import sg.edu.np.mad.Sharecipe.R;

public class ViewIngredientsAdapter extends RecyclerView.Adapter<ViewIngredientsViewholder> {

    public ViewIngredientsAdapter() {}

    @Override
    @NotNull
    public ViewIngredientsViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_viewingredients, parent, false);
        ViewIngredientsViewholder holder = new ViewIngredientsViewholder(item);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NotNull ViewIngredientsViewholder holder, int position) {
        // TODO: Bind stuff here
    }

    @Override
    public int getItemCount() {
        // TODO: Stuff
        int x = 1;
        return x;
    }
}
