package sg.edu.np.mad.Sharecipe.ui.main.discover;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.contants.IntentKeys;
import sg.edu.np.mad.Sharecipe.models.PartialRecipe;
import sg.edu.np.mad.Sharecipe.ui.view.RecipeViewActivity;

public class SmallCardViewHolder extends RecyclerView.ViewHolder {

    final TextView title;
    final ImageView image;
    final ProgressBar progressBar;

    int recipeId;

    public SmallCardViewHolder(@NonNull View itemView) {
        super(itemView);

        title = itemView.findViewById(R.id.discoverSmallTitle);
        image = itemView.findViewById(R.id.discoverSmallImage);
        progressBar = itemView.findViewById(R.id.imageLoadProgress);

        itemView.setOnClickListener(v -> {
            Intent viewRecipe = new Intent(itemView.getContext(), RecipeViewActivity.class);
            viewRecipe.putExtra(IntentKeys.RECIPE_VIEW, recipeId);
            itemView.getContext().startActivity(viewRecipe);
        });
    }
}
