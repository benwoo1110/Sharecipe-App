package sg.edu.np.mad.Sharecipe.ui.create;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.models.Ingredient;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsViewholder> {
    ArrayList<Ingredient> ingredients;
    Activity activity;

    public IngredientsAdapter(Activity activity) {
        this.activity = activity;
    }

    public IngredientsViewholder onCreateViewHolder(ViewGroup parent, int viewtype) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_ingredients, parent, false);
        IngredientsViewholder holder = new IngredientsViewholder(item);

        return holder;
    }

    public void onBindViewHolder(IngredientsViewholder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        ingredient.setName(holder.name.getText().toString());
        ingredient.setQuantity(Integer.parseInt(holder.quantity.getText().toString()));
        ingredient.setUnit(holder.unit.getText().toString());
    }

    public int getItemCount() {
        return ingredients.size();
    }

}

