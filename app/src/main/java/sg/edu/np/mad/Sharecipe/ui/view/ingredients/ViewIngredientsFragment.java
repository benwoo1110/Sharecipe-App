package sg.edu.np.mad.Sharecipe.ui.view.ingredients;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.models.Recipe;
import sg.edu.np.mad.Sharecipe.ui.common.data.DataLoadable;

public class ViewIngredientsFragment extends Fragment  implements DataLoadable<Recipe> {

    private final Recipe recipe;
    private RecyclerView recyclerView;
    private TextView emptyText;
    private ViewIngredientsAdapter adapter;

    public ViewIngredientsFragment(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_ingredient, container, false);

        recyclerView = view.findViewById(R.id.viewIngredients_recyclerView);
        emptyText = view.findViewById(R.id.empty_view_ingredients);

        adapter = new ViewIngredientsAdapter(new ArrayList<>());
        LinearLayoutManager cLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(cLayoutManager);

        onDataLoaded(recipe);

        return view;
    }

    @Override
    public void onDataLoaded(Recipe recipe) {
        adapter.setIngredientList(recipe.getIngredients());

        if (recipe.getIngredients().isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyText.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyText.setVisibility(View.GONE);
        }
    }
}
