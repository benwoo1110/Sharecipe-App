package sg.edu.np.mad.Sharecipe.ui.create.steps;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.contants.IntentKeys;
import sg.edu.np.mad.Sharecipe.models.RecipeStep;

public class StepsAdapter extends RecyclerView.Adapter<StepsViewHolder> {

    private final List<RecipeStep> stepList;
    private final Activity activity;

    public StepsAdapter(List<RecipeStep> stepList, Activity activity) {
        this.stepList = stepList;
        this.activity = activity;
    }

    @NotNull
    public StepsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_recipe_steps, parent, false);
        StepsViewHolder holder = new StepsViewHolder(item);

        item.setOnClickListener(v -> {
            Intent intent = new Intent(activity, StepsCreationActivity.class);
            intent.putExtra(IntentKeys.RECIPE_STEP_EDIT, holder.step);
            activity.startActivityForResult(intent, StepsFragment.LAUNCH_STEP_CREATION);
        });

        item.setOnLongClickListener(v -> {
            removeDialog(holder.step);
            return false;
        });

        return holder;
    }

    public void onBindViewHolder(StepsViewHolder holder, int position) {
        RecipeStep step = stepList.get(position);
        holder.stepNumber.setText(String.valueOf(step.getStepNumber()));
        holder.stepDescription.setText(step.getDescription());
        holder.step = stepList.get(position);
    }

    public int getItemCount() {
        return stepList.size();
    }

    public void removeDialog(RecipeStep stepToRemove) {
        new AlertDialog.Builder(activity, R.style.AlertDialogCustom)
                .setTitle("Remove step")
                .setMessage("Would you like to remove this step?")
                .setPositiveButton("Remove", (dialog, which) -> {
                    stepList.remove(stepToRemove);
                    notifyDataSetChanged();
                    for (RecipeStep step : stepList) {
                        step.setStepNumber(stepList.indexOf(step) + 1);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
