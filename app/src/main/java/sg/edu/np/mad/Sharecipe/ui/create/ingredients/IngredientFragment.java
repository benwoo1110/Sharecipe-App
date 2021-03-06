package sg.edu.np.mad.Sharecipe.ui.create.ingredients;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.models.Recipe;
import sg.edu.np.mad.Sharecipe.models.RecipeIngredient;
import sg.edu.np.mad.Sharecipe.ui.common.data.DataSaveable;

public class IngredientFragment extends Fragment implements DataSaveable<Recipe> {

    private final ArrayList<RecipeIngredient> ingredientsList = new ArrayList<>();
    private final Recipe recipe;

    public IngredientFragment(Recipe recipe){
        this.recipe = recipe;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_ingredient, container, false);

        Button addIngredient = view.findViewById(R.id.buttonIngredient);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerviewIngredient);

        IngredientsAdapter adapter = new IngredientsAdapter(ingredientsList, getActivity());
        LinearLayoutManager cLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(cLayoutManager);

        if (recipe.getIngredients() != null) {
            ingredientsList.addAll(recipe.getIngredients());
            adapter.notifyDataSetChanged();
        }

        addIngredient.setOnClickListener(v -> {
            RecipeIngredient newIngredient = new RecipeIngredient();
            newIngredient.setName("");
            newIngredient.setQuantity(1);
            newIngredient.setUnit("");
            ingredientsList.add(newIngredient);
            adapter.notifyDataSetChanged();
        });

        return view;
    }

    @Override
    public void onDataSaving(Recipe recipe) {
        recipe.setIngredients(ingredientsList);
    }
}