package sg.edu.np.mad.Sharecipe.ui.main.recipe;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.ChipGroup;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.contants.IntentKeys;
import sg.edu.np.mad.Sharecipe.models.Recipe;
import sg.edu.np.mad.Sharecipe.ui.view.RecipeViewActivity;

public class RecipeViewHolder extends RecyclerView.ViewHolder {

    final MaterialCardView card;
    final TextView title;
    final TextView info;
    final ImageView icon;
    final ChipGroup tags;
    final ProgressBar progressBar;

    Recipe recipe;

    public RecipeViewHolder(@NonNull View itemView) {
        super(itemView);

        card = itemView.findViewById(R.id.recipeCard);
        title = itemView.findViewById(R.id.recipeTitle);
        info = itemView.findViewById(R.id.recipeSecondaryInfo);
        icon = itemView.findViewById(R.id.recipeIconImage);
        tags = itemView.findViewById(R.id.recipeTopTags);
        progressBar = itemView.findViewById(R.id.imageLoadProgress);

        card.setOnClickListener(v -> {
            Intent viewRecipe = new Intent(itemView.getContext(), RecipeViewActivity.class);
            viewRecipe.putExtra(IntentKeys.RECIPE_VIEW, recipe.getRecipeId());
            itemView.getContext().startActivity(viewRecipe);
        });
    }
}
