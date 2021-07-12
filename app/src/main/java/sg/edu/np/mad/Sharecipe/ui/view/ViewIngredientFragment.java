package sg.edu.np.mad.Sharecipe.ui.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import sg.edu.np.mad.Sharecipe.R;

public class ViewIngredientFragment extends Fragment {

    public ViewIngredientFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_ingredient, container, false);

        return view;
    }
}
