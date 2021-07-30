package sg.edu.np.mad.Sharecipe.ui.view;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.Sharecipe.R;

public class ViewStepsViewholder extends RecyclerView.ViewHolder {

    TextView stepNumber;
    TextView stepDesc;

    public ViewStepsViewholder(View itemView) {
        super(itemView);

        stepNumber = itemView.findViewById(R.id.displayStepNumber_view);
        stepDesc = itemView.findViewById(R.id.displayStepDescription_view);
    }
}
