package sg.edu.np.mad.Sharecipe.ui.create;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.models.RecipeIngredient;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsViewholder> {
    ArrayList<RecipeIngredient> ingredients;
    Activity activity;

    public IngredientsAdapter(ArrayList<RecipeIngredient> ingredients, Activity activity) {
        this.ingredients = ingredients;
        this.activity = activity;
    }

    public IngredientsViewholder onCreateViewHolder(ViewGroup parent, int viewtype) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_ingredients, parent, false);
        IngredientsViewholder holder = new IngredientsViewholder(item);

        return holder;
    }

    public void onBindViewHolder(IngredientsViewholder holder, int position) {
        // TODO: Bind input to recyclerview
    }

    public int getItemCount() {
        return ingredients.size();
    }

}

