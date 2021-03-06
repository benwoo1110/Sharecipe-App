package sg.edu.np.mad.Sharecipe.ui.create.steps;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.models.RecipeStep;

public class StepsViewHolder extends RecyclerView.ViewHolder {

    TextView stepNumber;
    TextView stepDescription;
    RecipeStep step;

    public StepsViewHolder(View itemView){
        super(itemView);
        stepNumber = itemView.findViewById(R.id.displayStepNumber);
        stepDescription = itemView.findViewById(R.id.displayStepDescription);
    }
}
