package sg.edu.np.mad.Sharecipe.ui.view.ingredients;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.google.common.base.Strings;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.models.RecipeIngredient;
import sg.edu.np.mad.Sharecipe.models.RecipeStep;

public class ViewIngredientsAdapter extends RecyclerView.Adapter<ViewIngredientsViewholder> {
    List<RecipeIngredient> ingredients;

    public ViewIngredientsAdapter(List<RecipeIngredient> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    @NotNull
    public ViewIngredientsViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_view_ingredients, parent, false);
        return new ViewIngredientsViewholder(item);
    }

    @Override
    public void onBindViewHolder(@NotNull ViewIngredientsViewholder holder, int position) {
        RecipeIngredient ingredient = ingredients.get(position);
        holder.name.setText(ingredient.getName());
        if (Strings.isNullOrEmpty(ingredient.getUnit())) {
            holder.quantity.setText(String.format(Locale.ENGLISH, "%s %s", ingredient.getQuantity(), ""));
        }
        else {
            holder.quantity.setText(String.format(Locale.ENGLISH, "%s %s", ingredient.getQuantity(), ingredient.getUnit()));
        }
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public void setIngredientList(List<RecipeIngredient> ingredients) {
        this.ingredients.clear();
        this.ingredients.addAll(ingredients);
        notifyDataSetChanged();
    }
}
