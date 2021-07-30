package sg.edu.np.mad.Sharecipe.ui.create.ingredients;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.models.RecipeIngredient;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsViewHolder> {

    private final ArrayList<RecipeIngredient> ingredients;
    private final Activity activity;

    public IngredientsAdapter(ArrayList<RecipeIngredient> ingredients, Activity activity) {
        this.ingredients = ingredients;
        this.activity = activity;
    }

    @Override
    @NotNull
    public IngredientsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_recipe_ingredients, parent, false);
        IngredientsViewHolder holder = new IngredientsViewHolder(item, ingredients);
        item.setOnLongClickListener(v -> {
            removeDialog(holder.ingredient);
            return false;
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(IngredientsViewHolder holder, int position) {
        RecipeIngredient ingredient = ingredients.get(position);
        holder.name.setText(ingredient.getName());
        holder.quantity.setText(String.valueOf(ingredient.getQuantity()));
        holder.unit.setText(ingredient.getUnit());
        holder.number.setText(String.valueOf(ingredients.indexOf(ingredient) + 1));
        holder.ingredient = ingredients.get(position);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public void removeDialog(RecipeIngredient ingredient) {
        new AlertDialog.Builder(activity)
                .setTitle("Remove ingredient")
                .setMessage("Would you like to remove this ingredient?")
                .setPositiveButton("Remove", (dialog, which) -> {
                    ingredients.remove(ingredient);
                    notifyDataSetChanged();
                })
                .setNegativeButton("No", null)
                .show();
    }
}
