package sg.edu.np.mad.Sharecipe.ui.main.discover;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.models.PartialRecipe;
import sg.edu.np.mad.Sharecipe.ui.App;

public class LargeCardAdapter extends RecyclerView.Adapter<LargeCardViewHolder> {

    private final List<PartialRecipe> recipes;

    public LargeCardAdapter(List<PartialRecipe> recipes) {
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public LargeCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_discover_large, parent, false);
        return new LargeCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LargeCardViewHolder holder, int position) {
        PartialRecipe recipe = recipes.get(position);
        holder.title.setText(recipe.getName());
        if (recipe.getIcon() != null) {
            App.getRecipeManager().getIcon(recipe)
                    .onSuccess(bm ->  {
                        new Handler(Looper.getMainLooper()).post(() -> holder.image.setImageBitmap(bm));
                    });
        } else {
            holder.image.setImageResource(R.drawable.ic_baseline_fastfood_24);
        }
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }
}
