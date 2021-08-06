package sg.edu.np.mad.Sharecipe.ui.main.discover;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.contants.IntentKeys;
import sg.edu.np.mad.Sharecipe.ui.view.RecipeViewActivity;

public class LargeCardViewHolder extends RecyclerView.ViewHolder {

    final MaterialCardView card;
    final TextView title;
    final ImageView image;
    final ProgressBar progressBar;

    int recipeId;

    public LargeCardViewHolder(@NonNull View itemView) {
        super(itemView);

        card = itemView.findViewById(R.id.discoverLargeCard);
        title = itemView.findViewById(R.id.discoverLargeTitle);
        image = itemView.findViewById(R.id.discoverLargeImage);
        progressBar = itemView.findViewById(R.id.imageLoadProgress);

        card.setOnClickListener(v -> {
            Intent viewRecipe = new Intent(itemView.getContext(), RecipeViewActivity.class);
            viewRecipe.putExtra(IntentKeys.RECIPE_VIEW, recipeId);
            itemView.getContext().startActivity(viewRecipe);
        });
    }
}
