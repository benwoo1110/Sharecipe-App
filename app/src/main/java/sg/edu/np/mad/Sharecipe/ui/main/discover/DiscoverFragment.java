package sg.edu.np.mad.Sharecipe.ui.main.discover;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.ui.common.SectionAdapter;

public class DiscoverFragment extends Fragment {

    public DiscoverFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_discover, container, false);

        RecyclerView discoverRecyclerView = view.findViewById(R.id.discoverRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        SectionAdapter adapter = new SectionAdapter();
        adapter.addSection(new LargeSectionCreator("Latest"));
        adapter.addSection(new SmallSectionCreator("Greens"));
        adapter.addSection(new SmallSectionCreator("Meat"));
        discoverRecyclerView.setLayoutManager(layoutManager);
        discoverRecyclerView.setAdapter(adapter);

        return view;
    }
}