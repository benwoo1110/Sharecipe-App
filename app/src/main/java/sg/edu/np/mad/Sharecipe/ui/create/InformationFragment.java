package sg.edu.np.mad.Sharecipe.ui.create;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import java9.util.function.Consumer;
import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.data.RecipeManager;
import sg.edu.np.mad.Sharecipe.models.Account;
import sg.edu.np.mad.Sharecipe.models.Recipe;
import sg.edu.np.mad.Sharecipe.models.RecipeStep;


public class InformationFragment extends Fragment {

    public InformationFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_information, container, false);

        EditText name = view.findViewById(R.id.nameInput);
        CheckBox status = view.findViewById(R.id.publicCheck);
        RatingBar difficulty = view.findViewById(R.id.difficultyInput);
        EditText portion = view.findViewById(R.id.servesInput);
        EditText prepTime = view.findViewById(R.id.prepInput);
        ImageButton image = view.findViewById(R.id.addImage);

        return view;
    }

}
