package sg.edu.np.mad.Sharecipe.ui.create;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.models.RecipeStep;

public class StepsAdapter extends RecyclerView.Adapter<StepsViewholder> {
    ArrayList<RecipeStep> data;

    public StepsAdapter(ArrayList<RecipeStep> input) {this.data = input;}

    public StepsViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_steps, parent, false);
        StepsViewholder holder = new StepsViewholder(item);

        return holder;
    }

    public void onBindViewHolder(StepsViewholder holder, int position) {
        RecipeStep step = data.get(position);
        holder.stepNumber.setText(step.getStepNumber());
        holder.stepDescription.setText(step.getDescription());
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Add action for editing step
            }
        });
    }

    public int getItemCount() {
        return data.size();
    }
}
