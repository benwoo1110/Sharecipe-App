package sg.edu.np.mad.Sharecipe.ui.view.steps;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.models.Recipe;
import sg.edu.np.mad.Sharecipe.models.RecipeStep;

public class ViewStepsAdapter extends RecyclerView.Adapter<ViewStepsViewholder> {

    List<RecipeStep> steps;

    public ViewStepsAdapter(List<RecipeStep> steps) {
        this.steps = steps;
    }

    @Override
    @NotNull
    public ViewStepsViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_recipe_steps, parent, false);
        return new ViewStepsViewholder(item);
    }

    @Override
    public void onBindViewHolder(@NotNull ViewStepsViewholder holder, int position) {
        RecipeStep step = steps.get(position);

        holder.stepNumber.setText(String.valueOf(step.getStepNumber()));
        holder.stepDesc.setText(step.getDescription());

    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public void setStepList(List<RecipeStep> stepList) {
        this.steps.clear();
        this.steps.addAll(stepList);
        notifyDataSetChanged();
    }
}
