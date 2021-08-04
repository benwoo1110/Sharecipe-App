package sg.edu.np.mad.Sharecipe.ui.main.discover;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.facebook.shimmer.ShimmerFrameLayout;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.models.Discover;
import sg.edu.np.mad.Sharecipe.models.DiscoverSection;
import sg.edu.np.mad.Sharecipe.ui.App;
import sg.edu.np.mad.Sharecipe.ui.common.SectionAdapter;
import sg.edu.np.mad.Sharecipe.utils.FutureDataResult;

public class DiscoverFragment extends Fragment {

    private ShimmerFrameLayout shimmerFrameLayout;
    private SectionAdapter adapter;
    private SwipeRefreshLayout discoverRefresh;
    private RecyclerView discoverRecyclerView;

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

        discoverRecyclerView = view.findViewById(R.id.discoverRecyclerView);
        shimmerFrameLayout = view.findViewById(R.id.discoverShimmerLayout);
        discoverRefresh = view.findViewById(R.id.discoverRefresh);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        adapter = new SectionAdapter();
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation_from_bottom);
        discoverRecyclerView.setLayoutManager(layoutManager);
        discoverRecyclerView.setAdapter(adapter);
        discoverRecyclerView.setLayoutAnimation(controller);

        discoverRefresh.setOnRefreshListener(this::loadDiscover);

        loadDiscover();

        return view;
    }

    private void loadDiscover() {
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        discoverRecyclerView.setVisibility(View.GONE);
        shimmerFrameLayout.startShimmer();
        App.getSearchManager().discover().onSuccess(discover -> {
            adapter = new SectionAdapter();
            for (DiscoverSection section : discover.getSections()) {
                switch (section.getSizeType()) {
                    case LARGE:
                        adapter.addSection(new LargeSectionCreator(section.getHeader(), section.getRecipes()));
                        break;
                    case SMALL:
                        adapter.addSection(new SmallSectionCreator(section.getHeader(), section.getRecipes()));
                        break;
                }
            }
            getActivity().runOnUiThread(() -> {
                discoverRecyclerView.setVisibility(View.VISIBLE);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                discoverRecyclerView.setAdapter(adapter);
                discoverRecyclerView.scheduleLayoutAnimation();
                discoverRefresh.setRefreshing(false);
            });
        });
    }
}