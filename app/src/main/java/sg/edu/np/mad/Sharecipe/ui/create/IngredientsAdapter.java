package sg.edu.np.mad.Sharecipe.ui.create;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.models.Ingredient;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsViewholder> {
    ArrayList<Ingredient> ingredients;

    public IngredientsAdapter() {}

    public IngredientsViewholder onCreateViewHolder(ViewGroup parent, int viewtype) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_ingredients, parent, false);
        IngredientsViewholder holder = new IngredientsViewholder(item);

        return holder;
    }

    public void onBindViewHolder(IngredientsViewholder holder, int position) {
        //TODO: stuff
    }

    public int getItemCount() {
        return ingredients.size();
    }

}

