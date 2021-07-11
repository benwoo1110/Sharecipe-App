package sg.edu.np.mad.Sharecipe.ui.create;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.models.RecipeStep;

public class StepsFragment extends Fragment {

    private StepsAdapter adapter;
    ArrayList<RecipeStep> stepsList = new ArrayList<>();
    public static int LAUNCH_STEP_CREATION = 1;

    public StepsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_steps, container, false);
        RecyclerView stepsView = view.findViewById(R.id.recyclerview_steps);
        Button button = view.findViewById(R.id.buttonAdd);

        adapter = new StepsAdapter(stepsList, getActivity());
        LinearLayoutManager cLayoutManager = new LinearLayoutManager(getActivity());

        stepsView.setAdapter(adapter);
        stepsView.setLayoutManager(cLayoutManager);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecipeStep newStep =  new RecipeStep();
                int position = stepsList.size() + 1;
                Intent intent = new Intent(getContext(), StepsCreation.class);
                intent.putExtra("New step", newStep);
                intent.putExtra("Step number", position);
                startActivityForResult(intent, LAUNCH_STEP_CREATION);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_STEP_CREATION) {
            if (resultCode == Activity.RESULT_OK) {
                RecipeStep inputStep = (RecipeStep) data.getSerializableExtra("Input step");
                stepsList.add(inputStep);
                adapter.notifyDataSetChanged();
            }
        }
    }
}
