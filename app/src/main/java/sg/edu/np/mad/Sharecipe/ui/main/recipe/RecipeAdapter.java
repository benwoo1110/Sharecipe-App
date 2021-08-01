package sg.edu.np.mad.Sharecipe.ui.main.recipe;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

import java.util.List;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.models.Recipe;
import sg.edu.np.mad.Sharecipe.models.RecipeTag;
import sg.edu.np.mad.Sharecipe.ui.App;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeViewHolder> {

    private final List<Recipe> recipeList;

    public RecipeAdapter(List<Recipe> recipeList) {
        this.recipeList = recipeList;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);
        holder.recipe = recipe;
        holder.title.setText(recipe.getName());

        //TODO display some cool stats
        holder.info.setText(String.valueOf(recipe.getRecipeId()));

        holder.tags.removeAllViews();
        if (recipe.getTags() != null) {
            int count = 0;
            for (RecipeTag tag : recipe.getTags()) {
                Chip chip = new Chip(holder.itemView.getContext());
                chip.setText(tag.getName());
                chip.setTextIsSelectable(true);
                holder.tags.addView(chip);
                if (++count > 2) {
                    break;
                }
            }
        }

        if (recipe.getIcon() != null) {
            holder.icon.setImageBitmap(null);
            holder.progressBar.setVisibility(View.VISIBLE);
            App.getRecipeManager().getIcon(recipe)
                    .onSuccess(bm ->  {
                        new Handler(Looper.getMainLooper()).post(() -> {
                            holder.progressBar.setVisibility(View.GONE);
                            holder.icon.setImageBitmap(bm);
                        });
                    });
        } else {
            holder.progressBar.setVisibility(View.GONE);
            holder.icon.setImageResource(R.drawable.ic_baseline_fastfood_24);
        }
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public List<Recipe> getRecipeList() {
        return recipeList;
    }

    public void setRecipeList(List<Recipe> recipeList) {
        this.recipeList.clear();
        this.recipeList.addAll(recipeList);
        notifyDataSetChanged();
    }

    public void addRecipe(Recipe recipe) {
        this.recipeList.add(recipe);
        notifyItemInserted(this.recipeList.size());
    }
}
