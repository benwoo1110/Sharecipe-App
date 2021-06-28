package sg.edu.np.mad.Sharecipe.ui.create;

import android.media.Rating;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import sg.edu.np.mad.Sharecipe.R;


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
