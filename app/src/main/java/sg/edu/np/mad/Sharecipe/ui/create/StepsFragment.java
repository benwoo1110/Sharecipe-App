package sg.edu.np.mad.Sharecipe.ui.create;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.models.RecipeStep;

public class StepsFragment extends Fragment {

    ArrayList<RecipeStep> stepsList = new ArrayList<>();

    public StepsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_steps, container, false);
        //RecyclerView stepsView = view.findViewById(R.id.recyclerview_steps);
        FloatingActionButton button = view.findViewById(R.id.buttonAdd);

        StepsAdapter adapter = new StepsAdapter(stepsList);
        LinearLayoutManager cLayoutManager = new LinearLayoutManager(getActivity());

        //stepsView.setAdapter(adapter);
        //stepsView.setLayoutManager(cLayoutManager);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }
}
