package sg.edu.np.mad.Sharecipe.ui.main.recipe;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.contants.IntentKeys;
import sg.edu.np.mad.Sharecipe.models.PartialRecipe;
import sg.edu.np.mad.Sharecipe.ui.view.RecipeViewActivity;

public class LikedRecipeViewHolder extends RecyclerView.ViewHolder{
    final TextView title;
    final TextView info;
    final ImageView icon;
    PartialRecipe recipe;
    final TextView likedNo;

    public LikedRecipeViewHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.recipeTitle);
        info = itemView.findViewById(R.id.recipeSecondaryInfo);
        icon = itemView.findViewById(R.id.recipeIconImage);
        likedNo = itemView.findViewById(R.id.likesNo);
        itemView.setOnClickListener(v -> {
            Intent viewRecipe = new Intent(itemView.getContext(), RecipeViewActivity.class);
            viewRecipe.putExtra(IntentKeys.RECIPE_VIEW, recipe.getRecipeId());
            itemView.getContext().startActivity(viewRecipe);
        });
    }

}
