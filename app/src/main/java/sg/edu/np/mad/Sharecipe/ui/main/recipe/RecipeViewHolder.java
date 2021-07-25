package sg.edu.np.mad.Sharecipe.ui.main.recipe;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.models.PartialRecipe;
import sg.edu.np.mad.Sharecipe.models.Recipe;
import sg.edu.np.mad.Sharecipe.ui.view.RecipeViewActivity;

public class RecipeViewHolder extends RecyclerView.ViewHolder {

    final TextView title;
    final TextView info;
    final ImageView icon;
    PartialRecipe recipe;

    public RecipeViewHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.recipeTitle);
        info = itemView.findViewById(R.id.recipeSecondaryInfo);
        icon = itemView.findViewById(R.id.recipeIconImage);
        itemView.setOnClickListener(v -> {
            Intent viewRecipe = new Intent(itemView.getContext(), RecipeViewActivity.class);
            viewRecipe.putExtra("Recipe", recipe);
            itemView.getContext().startActivity(viewRecipe);
        });
    }
}
