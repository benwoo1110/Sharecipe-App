package sg.edu.np.mad.Sharecipe.ui.create;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.Sharecipe.R;

public class StepsViewholder extends RecyclerView.ViewHolder {

    TextView stepNumber;
    TextView stepDescription;
    TextView timeNeeded;
    ImageButton edit;

    public StepsViewholder(View itemView){
        super(itemView);
        stepNumber = itemView.findViewById(R.id.stepNumber);
        stepDescription = itemView.findViewById(R.id.stepDescription);
        timeNeeded= itemView.findViewById(R.id.stepTimeneeded);
        edit = itemView.findViewById(R.id.stepEdit);
    }
}
