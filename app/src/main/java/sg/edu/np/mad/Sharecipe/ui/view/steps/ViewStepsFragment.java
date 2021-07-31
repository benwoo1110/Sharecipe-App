package sg.edu.np.mad.Sharecipe.ui.view.steps;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.models.Recipe;

public class ViewStepsFragment extends Fragment {

    private final Recipe recipe;

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

        RecyclerView recyclerView = view.findViewById(R.id.viewSteps_recyclerView);
        TextView emptyText = view.findViewById(R.id.empty_view_steps);

        ViewStepsAdapter adapter = new ViewStepsAdapter(recipe.getSteps());
        LinearLayoutManager cLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(cLayoutManager);

        if (recipe.getSteps().isEmpty()) {
            recyclerView.setVisibility(view.GONE);
            emptyText.setVisibility(view.VISIBLE);
        }

        return view;
    }
}
