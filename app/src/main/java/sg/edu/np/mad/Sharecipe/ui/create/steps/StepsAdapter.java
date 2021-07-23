package sg.edu.np.mad.Sharecipe.ui.create.steps;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.models.RecipeStep;

public class StepsAdapter extends RecyclerView.Adapter<StepsViewHolder> {

    private final ArrayList<RecipeStep> data;
    private final Activity activity;

    public StepsAdapter(ArrayList<RecipeStep> input, Activity activity) {
        this.data = input;
        this.activity = activity;
    }

    @NotNull
    public StepsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_steps, parent, false);
        StepsViewHolder holder = new StepsViewHolder(item);

        holder.stepDescription.setOnClickListener(v -> {
            Intent intent = new Intent(activity, StepsCreationActivity.class);
            intent.putExtra("New step", holder.step);
            intent.putExtra("Edit description", holder.step.getDescription());
            intent.putExtra("Step number", holder.step.getStepNumber());
            activity.startActivity(intent);
        });

        holder.stepDescription.setOnLongClickListener(v -> {
            removeDialog(holder.step);
            return false;
        });

        return holder;
    }

    public void onBindViewHolder(StepsViewHolder holder, int position) {
        RecipeStep step = data.get(position);
        holder.stepNumber.setText(String.valueOf(step.getStepNumber()));
        holder.stepDescription.setText(step.getDescription());
        holder.step = data.get(position);
    }

    public int getItemCount() {
        return data.size();
    }

    public void removeDialog(RecipeStep stepToRemove) {
        new AlertDialog.Builder(activity)
                .setTitle("Remove step")
                .setMessage("Would you like to remove this step?")
                .setPositiveButton("Remove", (dialog, which) -> {
                    data.remove(stepToRemove);
                    notifyDataSetChanged();
                    for (RecipeStep step : data) {
                        step.setStepNumber(data.indexOf(step) + 1);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
