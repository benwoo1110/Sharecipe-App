package sg.edu.np.mad.Sharecipe.ui.main.discover;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.models.DiscoverSection;
import sg.edu.np.mad.Sharecipe.ui.App;
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
        discoverRecyclerView.setLayoutManager(layoutManager);
        discoverRecyclerView.setAdapter(adapter);

        App.getSearchManager().discover().onSuccess(discover -> {
            for (DiscoverSection section : discover.getSections()) {
                switch (section.getSizeType()) {
                    case LARGE:
                        adapter.addSection(new LargeSectionCreator(section.getHeader(), section.getRecipes()));
                        break;
                    case NORMAL:
                        adapter.addSection(new SmallSectionCreator(section.getHeader(), section.getRecipes()));
                        break;
                }
            }
            getActivity().runOnUiThread(adapter::notifyDataSetChanged);
        });

        return view;
    }
}