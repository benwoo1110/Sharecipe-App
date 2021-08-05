package sg.edu.np.mad.Sharecipe.ui.create.steps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.contants.IntentKeys;
import sg.edu.np.mad.Sharecipe.models.Recipe;
import sg.edu.np.mad.Sharecipe.models.RecipeStep;
import sg.edu.np.mad.Sharecipe.ui.common.SwipeToDeleteCallback;

public class StepsFragment extends Fragment {

    public static int LAUNCH_STEP_CREATION = 1;

    private StepsAdapter adapter;
    private final List<RecipeStep> stepsList = new ArrayList<>();
    private final Recipe recipe;
    private RecyclerView stepsView;

    public StepsFragment(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_steps, container, false);

        stepsView = view.findViewById(R.id.recyclerview_steps);
        Button button = view.findViewById(R.id.buttonAdd);

        adapter = new StepsAdapter(stepsList, getActivity());
        LinearLayoutManager cLayoutManager = new LinearLayoutManager(getActivity());

        stepsView.setAdapter(adapter);
        stepsView.setLayoutManager(cLayoutManager);
        enableSwipeToDelete();

        if (recipe.getSteps() != null) {
            for (RecipeStep step : recipe.getSteps()
                 ) {
                stepsList.add(step);
                adapter.notifyDataSetChanged();
            }
        }

        button.setOnClickListener(v -> {
            RecipeStep newStep =  new RecipeStep();
            newStep.setStepNumber(stepsList.size() + 1);
            Intent intent = new Intent(getContext(), StepsCreationActivity.class);
            intent.putExtra(IntentKeys.RECIPE_STEP_EDIT, newStep);
            startActivityForResult(intent, LAUNCH_STEP_CREATION);
        });

        recipe.setSteps(stepsList);

        return view;
    }

    private void enableSwipeToDelete() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                stepsList.remove(viewHolder.getPosition());
                int stepCount = 1;
                for (RecipeStep step : stepsList) {
                    step.setStepNumber(stepCount++);
                }
                adapter.notifyDataSetChanged();
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(stepsView);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != LAUNCH_STEP_CREATION) {
            return;
        }

        if (resultCode == Activity.RESULT_OK) {
            RecipeStep inputStep = (RecipeStep) data.getSerializableExtra(IntentKeys.RECIPE_STEP_SAVE);
            System.out.println(inputStep);
            int targetListIndex = inputStep.getStepNumber() - 1;
            if (inputStep.getStepNumber() <= stepsList.size()) {
                stepsList.set(targetListIndex, inputStep);
                adapter.notifyItemChanged(targetListIndex);
            } else {
                stepsList.add(inputStep);
                adapter.notifyItemInserted(targetListIndex);
            }
        }
    }
}
