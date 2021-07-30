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

public class SmallCardAdapter extends RecyclerView.Adapter<SmallCardViewHolder> {

    private final List<PartialRecipe> recipes;

    public SmallCardAdapter(List<PartialRecipe> recipes) {
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public SmallCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_discover_small, parent, false);
        return new SmallCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SmallCardViewHolder holder, int position) {
        PartialRecipe recipe = recipes.get(position);
        holder.recipe = recipe;
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
