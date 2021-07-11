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
        holder.stepNumber.setText(String.valueOf(step.getStepNumber()));
        holder.stepDescription.setText(step.getDescription());

        if (step.getTimeNeeded() <= 59) {
            holder.timeNeeded.setText(step.getTimeNeeded() + " seconds");
        }

        else if (step.getTimeNeeded() <= 3599) {
            int minutes = (step.getTimeNeeded() - step.getTimeNeeded() % 60) / 60;
            int seconds = step.getTimeNeeded() - (minutes * 60);

            if (seconds == 0) {
                holder.timeNeeded.setText(minutes + " minutes ");
            }

            else {
                holder.timeNeeded.setText(minutes + " minutes " + seconds + " seconds");
            }

        }

        else {
            int hours = (step.getTimeNeeded() - step.getTimeNeeded() % 3600) / 3600;
            int x = (step.getTimeNeeded() / 60) - (hours * 60) ;
            int minutes = x - (((x * 60) % 60) / 60);
            int seconds = (step.getTimeNeeded() - (hours * 3600) - (minutes * 60));

            if (minutes == 0) {
                if (seconds == 0) {
                    holder.timeNeeded.setText(hours + " hours");
                }
                else {
                    holder.timeNeeded.setText(hours + " hours " + seconds + " seconds ");
                }
            }

            else if (seconds == 0) {
                holder.timeNeeded.setText(hours + " hours " + minutes + " minutes");
            }

            else {
                holder.timeNeeded.setText(hours + " hours " + minutes + " minutes " + seconds + " seconds") ;
            }

        }
    }

    public int getItemCount() {
        return data.size();
    }

}
