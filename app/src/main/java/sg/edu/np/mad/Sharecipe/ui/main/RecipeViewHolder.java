package sg.edu.np.mad.Sharecipe.ui.main;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.Sharecipe.R;

public class RecipeViewHolder extends RecyclerView.ViewHolder {

    final TextView title;
    final TextView info;

    public RecipeViewHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.recipeTitle);
        info = itemView.findViewById(R.id.recipeSecondaryInfo);
    }
}
