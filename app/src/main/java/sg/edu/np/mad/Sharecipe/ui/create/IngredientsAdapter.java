package sg.edu.np.mad.Sharecipe.ui.create;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.models.RecipeIngredient;
import sg.edu.np.mad.Sharecipe.models.RecipeStep;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsViewholder> {
    ArrayList<RecipeIngredient> ingredients;
    Activity activity;

    public IngredientsAdapter(ArrayList<RecipeIngredient> ingredients, Activity activity) {
        this.ingredients = ingredients;
        this.activity = activity;
    }

    public IngredientsViewholder onCreateViewHolder(ViewGroup parent, int viewtype) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_ingredients, parent, false);
        IngredientsViewholder holder = new IngredientsViewholder(item, ingredients);

        holder.number.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                removeDialog(holder.ingredient);
                return false;
            }
        });

        return holder;
    }

    public void onBindViewHolder(IngredientsViewholder holder, int position) {
        RecipeIngredient ingredient = ingredients.get(position);
        holder.name.setText(ingredient.getName());
        holder.quantity.setText(String.valueOf(ingredient.getQuantity()));
        holder.unit.setText(ingredient.getUnit());
        holder.number.setText(String.valueOf(ingredients.indexOf(ingredient) + 1));
        holder.ingredient = ingredients.get(position);
    }

    public int getItemCount() {
        return ingredients.size();
    }

    public void removeDialog(RecipeIngredient ingredient) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Remove ingredient");
        builder.setMessage("Would you like to remove this ingredient?");
        builder.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ingredients.remove(ingredient);
                notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

}

