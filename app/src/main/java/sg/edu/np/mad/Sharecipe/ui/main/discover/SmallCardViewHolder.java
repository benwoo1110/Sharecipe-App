package sg.edu.np.mad.Sharecipe.ui.main.discover;

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

public class SmallCardViewHolder extends RecyclerView.ViewHolder {

    final TextView title;
    final ImageView image;
    PartialRecipe recipe;

    public SmallCardViewHolder(@NonNull View itemView) {
        super(itemView);

        title = itemView.findViewById(R.id.discoverSmallTitle);
        image = itemView.findViewById(R.id.discoverSmallImage);

        itemView.setOnClickListener(v -> {
            Intent viewRecipe = new Intent(itemView.getContext(), RecipeViewActivity.class);
            viewRecipe.putExtra(IntentKeys.RECIPE_VIEW, recipe);
            itemView.getContext().startActivity(viewRecipe);
        });
    }
}
