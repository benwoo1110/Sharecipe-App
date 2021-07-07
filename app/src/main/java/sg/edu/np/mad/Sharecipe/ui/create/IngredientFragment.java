package sg.edu.np.mad.Sharecipe.ui.create;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import sg.edu.np.mad.Sharecipe.R;

public class IngredientFragment extends Fragment {

    public IngredientFragment(){}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ingredient, container, false);
        Button createSection = view.findViewById(R.id.buttonIngredient);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerviewIngredient);

        IngredientsAdapter adapter = new IngredientsAdapter(getActivity());
        LinearLayoutManager cLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(cLayoutManager);

        createSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Stuff
            }
        });

        return view;
    }
}