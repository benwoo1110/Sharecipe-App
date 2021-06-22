package sg.edu.np.mad.Sharecipe.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import sg.edu.np.mad.Sharecipe.R;

public class StepsFragment extends Fragment {

    public StepsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_steps, container, false);
    }
}
