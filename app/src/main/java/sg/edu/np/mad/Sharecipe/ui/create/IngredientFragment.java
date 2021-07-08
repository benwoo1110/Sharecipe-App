package sg.edu.np.mad.Sharecipe.ui.create;

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
import sg.edu.np.mad.Sharecipe.models.RecipeIngredient;

// TODO: Saving and storing of values for all input fields along with input validation (unit of measurement must be string?)
// TODO: Option to delete ingredients (list item selection OR small cross button)
// TODO: Touch up on UI

public class IngredientFragment extends Fragment {

    private final ArrayList<RecipeIngredient> ingredientsList = new ArrayList<>();

    public IngredientFragment(){}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ingredient, container, false);
        Button addIngredient = view.findViewById(R.id.buttonIngredient);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerviewIngredient);

        IngredientsAdapter adapter = new IngredientsAdapter(ingredientsList, getActivity());
        LinearLayoutManager cLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(cLayoutManager);

        addIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Create new ingredient, add to list
                RecipeIngredient newIngredient = new RecipeIngredient();
                newIngredient.setName("");
                newIngredient.setQuantity(1);
                newIngredient.setUnit("");
                ingredientsList.add(newIngredient);
                adapter.notifyDataSetChanged();

            }
        });

        return view;
    }
}