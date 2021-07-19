package sg.edu.np.mad.Sharecipe.ui.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.models.Recipe;

public class ViewInformationFragment extends Fragment {

    private final Recipe recipe;

    public ViewInformationFragment(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_information, container, false);
        TextInputEditText displayName = view.findViewById(R.id.viewRecipeName);
        TextInputEditText displayPortion = view.findViewById(R.id.viewPortion);
        TextInputEditText displayPrep = view.findViewById(R.id.viewPrep);

        // Get recipe information and store to variables
        String name = recipe.getName();
        int portions = recipe.getPortion();
        int prepSeconds = recipe.getTotalTimeNeeded();

        // Set stored variables to android widgets
        // Set name of recipe
        displayName.setText(name);

        // Set number of portions, display not specified if it is 0
        if (portions > 0) {
            displayPortion.setText("Serves " + String.valueOf(portions));
        }
        else {
            displayPortion.setText("not specified");
        }

        if (prepSeconds < 0) {
            displayPrep.setText("not specified");
        }

        else if (prepSeconds < 3600) {
            int minutes = prepSeconds / 60;
            displayPrep.setText(String.valueOf(minutes) + " minutes");
        }

        else {
            int prepMinutes = prepSeconds / 60;
            int hours = (prepMinutes - prepMinutes % 60) / 60;
            int minutes = prepMinutes - (hours * 60);
            displayPrep.setText(String.valueOf(hours) + " hours " + String.valueOf(minutes) + " minutes");
        }


        return view;
    }

}
