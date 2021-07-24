package sg.edu.np.mad.Sharecipe.ui.main.recipe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.models.PartialRecipe;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeViewHolder> {

    private final List<PartialRecipe> recipeList;

    public RecipeAdapter(List<PartialRecipe> recipeList) {
        this.recipeList = recipeList;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_recipe, parent, false);
        return new RecipeViewHolder(view, recipeList);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        PartialRecipe recipe = recipeList.get(position);
        holder.title.setText(recipe.getName());
        holder.info.setText(String.valueOf(recipe.getRecipeId()));
        if (recipe.getIconEE() != null) {
            holder.icon.setImageBitmap(recipe.getIconEE());
        } else {
            holder.icon.setImageResource(R.drawable.ic_baseline_fastfood_24);
        }
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public List<PartialRecipe> getRecipeList() {
        return recipeList;
    }

    public void setRecipeList(List<PartialRecipe> recipeList) {
        this.recipeList.clear();
        this.recipeList.addAll(recipeList);
        notifyDataSetChanged();
    }

    public void addRecipe(PartialRecipe recipe) {
        this.recipeList.add(recipe);
        notifyItemInserted(this.recipeList.size());
    }
}
