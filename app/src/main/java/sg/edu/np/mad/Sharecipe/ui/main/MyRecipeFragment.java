package sg.edu.np.mad.Sharecipe.ui.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sg.edu.np.mad.Sharecipe.R;

public class MyRecipeFragment extends Fragment {

    public MyRecipeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_recipe, container, false);
        RecyclerView recipeRecyclerView = view.findViewById(R.id.myRecipeRecyclerView);
        RecipeAdapter adapter = new RecipeAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        recipeRecyclerView.setAdapter(adapter);
        recipeRecyclerView.setLayoutManager(layoutManager);

        return view;
    }
}