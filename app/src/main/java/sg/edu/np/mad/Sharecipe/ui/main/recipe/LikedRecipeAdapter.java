package sg.edu.np.mad.Sharecipe.ui.main.recipe;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.data.RecipeManager;
import sg.edu.np.mad.Sharecipe.models.PartialRecipe;
import sg.edu.np.mad.Sharecipe.ui.App;
import sg.edu.np.mad.Sharecipe.ui.main.MainActivity;

public class LikedRecipeAdapter extends RecyclerView.Adapter<LikedRecipeViewHolder> {
    private final List<PartialRecipe> recipeList;
    int likes;
    public LikedRecipeAdapter(List<PartialRecipe> recipeList) {
        this.recipeList = recipeList;
    }

    @NonNull
    @Override
    public LikedRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_liked_recipe, parent, false);
        return new LikedRecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LikedRecipeViewHolder holder, int position) {

        PartialRecipe recipe = recipeList.get(position);
        holder.recipe = recipe;
        holder.title.setText(recipe.getName());
        holder.info.setText(String.valueOf(recipe.getRecipeId()));
        //RecipeManager.getInstance(this.context).getLikes(recipe).onSuccess(recipeLikes -> {
        //    likes = recipeLikes.size();
        //});
        holder.likedNo.setText(String.valueOf(likes));

        if (recipe.getIcon() != null) {
            App.getRecipeManager().getIcon(recipe)
                    .onSuccess(bm ->  {
                        new Handler(Looper.getMainLooper()).post(() -> holder.icon.setImageBitmap(bm));
                    });
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
