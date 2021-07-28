package sg.edu.np.mad.Sharecipe.ui.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_viewingredients, parent, false);
        ViewIngredientsViewholder holder = new ViewIngredientsViewholder(item);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NotNull ViewIngredientsViewholder holder, int position) {
        RecipeIngredient ingredient = ingredients.get(position);
        holder.number.setText(String.valueOf(ingredients.indexOf(ingredient) + 1));
        holder.name.setText(ingredient.getName());
        holder.quantity.setText(String.valueOf(ingredient.getQuantity()));
        holder.unit.setText(ingredient.getUnit());

    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }
}
