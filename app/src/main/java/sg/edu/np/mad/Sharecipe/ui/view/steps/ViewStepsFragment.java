package sg.edu.np.mad.Sharecipe.ui.view.steps;

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

public class ViewStepsFragment extends Fragment implements DataLoadable<Recipe> {

    private final Recipe recipe;
    private RecyclerView recyclerView;
    private TextView emptyText;
    private ViewStepsAdapter adapter;

    public ViewStepsFragment(Recipe recipe) {
        this.recipe = recipe;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_steps, container, false);

        recyclerView = view.findViewById(R.id.viewSteps_recyclerView);
        emptyText = view.findViewById(R.id.empty_view_steps);

        adapter = new ViewStepsAdapter(new ArrayList<>());
        LinearLayoutManager cLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(cLayoutManager);

        onDataLoaded(recipe);

        return view;
    }

    @Override
    public void onDataLoaded(Recipe recipe) {
        adapter.setStepList(recipe.getSteps());

        if (recipe.getSteps().isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyText.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyText.setVisibility(View.GONE);
        }
    }
}
