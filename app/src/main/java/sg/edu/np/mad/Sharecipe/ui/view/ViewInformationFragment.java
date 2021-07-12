package sg.edu.np.mad.Sharecipe.ui.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

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
        TextView displayName = view.findViewById(R.id.viewRecipeName);

        String name = recipe.getName();
        displayName.setText(name);


        return view;
    }
}
