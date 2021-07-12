package sg.edu.np.mad.Sharecipe.ui.create.steps;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.models.RecipeStep;

public class StepsAdapter extends RecyclerView.Adapter<StepsViewholder> {
    ArrayList<RecipeStep> data;
    Activity activity;
    public static int EDIT_STEP = 2;

    public StepsAdapter(ArrayList<RecipeStep> input, Activity activity) {
        this.data = input;
        this.activity = activity;
    }

    public StepsViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_steps, parent, false);
        StepsViewholder holder = new StepsViewholder(item);

        holder.stepDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, StepsCreation.class);
                intent.putExtra("New step", holder.step);
                intent.putExtra("Edit description", holder.step.getDescription());
                intent.putExtra("Step number", holder.step.getStepNumber());
                intent.putExtra("Edit time", holder.step.getTimeNeeded());
                activity.startActivity(intent);
            }
        });

        holder.stepDescription.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                removeDialog(holder.step);
                return false;
            }
        });

        return holder;
    }

    public void onBindViewHolder(StepsViewholder holder, int position) {
        RecipeStep step = data.get(position);
        holder.stepNumber.setText(String.valueOf(step.getStepNumber()));
        holder.stepDescription.setText(step.getDescription());
        holder.step = data.get(position);

        if (step.getTimeNeeded() == 0) {
            holder.timeNeeded.setText("");
        }

        else if (step.getTimeNeeded() <= 59) {
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

    public void removeDialog(RecipeStep step) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Remove step");
        builder.setMessage("Would you like to remove this step?");
        builder.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                data.remove(step);
                notifyDataSetChanged();
                for (RecipeStep step : data
                     ) {
                    step.setStepNumber(data.indexOf(step) + 1);
                }
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
