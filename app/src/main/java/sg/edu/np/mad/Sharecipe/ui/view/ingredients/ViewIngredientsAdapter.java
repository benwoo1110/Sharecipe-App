package sg.edu.np.mad.Sharecipe.ui.view.ingredients;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.models.RecipeIngredient;

public class ViewIngredientsAdapter extends RecyclerView.Adapter<ViewIngredientsViewholder> {
    List<RecipeIngredient> ingredients;

    public ViewIngredientsAdapter(List<RecipeIngredient> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    @NotNull
    public ViewIngredientsViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_view_ingredients, parent, false);
        ViewIngredientsViewholder holder = new ViewIngredientsViewholder(item);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NotNull ViewIngredientsViewholder holder, int position) {
        RecipeIngredient ingredient = ingredients.get(position);
        holder.name.setText(ingredient.getName());
        holder.quantity.setText(String.format(Locale.ENGLISH, "%d %s", ingredient.getQuantity(), ingredient.getUnit()));

    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }
}
