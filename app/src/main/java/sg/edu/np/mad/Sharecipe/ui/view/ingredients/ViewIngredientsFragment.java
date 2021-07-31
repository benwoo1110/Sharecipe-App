package sg.edu.np.mad.Sharecipe.ui.view.ingredients;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.models.Recipe;

public class ViewIngredientsFragment extends Fragment {

    private final Recipe recipe;

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

        RecyclerView recyclerView = view.findViewById(R.id.viewIngredients_recyclerView);

        ViewIngredientsAdapter adapter = new ViewIngredientsAdapter(recipe.getIngredients());
        LinearLayoutManager cLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(cLayoutManager);

        return view;
    }
}
