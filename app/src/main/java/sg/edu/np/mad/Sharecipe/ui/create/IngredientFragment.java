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
import sg.edu.np.mad.Sharecipe.models.Ingredient;

public class IngredientFragment extends Fragment {

    private final ArrayList<Ingredient> ingredientsList = new ArrayList<>();

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

            }
        });

        return view;
    }
}