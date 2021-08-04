package sg.edu.np.mad.Sharecipe.ui.main.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.base.Strings;

import java.util.ArrayList;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.data.UserManager;
import sg.edu.np.mad.Sharecipe.models.User;
import sg.edu.np.mad.Sharecipe.ui.App;
import sg.edu.np.mad.Sharecipe.ui.common.UiHelper;
import sg.edu.np.mad.Sharecipe.ui.main.profile.stats.StatsAdapter;

public class PartialProfileFragment extends Fragment {

    private final int userId;

    private User user;
    private OnUserLoadedListener userLoadedListener;

    private boolean isFirstTime = true;
    private TextView username;
    private TextView description;
    private ImageView profileImage;
    private RecyclerView gridStatsView;
    private StatsAdapter adapter;

    public PartialProfileFragment(int userId) {
        this.userId = userId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_partial_view_profile, container, false);

        username = view.findViewById(R.id.username);
        description = view.findViewById(R.id.description);
        profileImage = view.findViewById(R.id.profileImage);
        gridStatsView = view.findViewById(R.id.statsRecyclerView);

        adapter = new StatsAdapter(user, new ArrayList<>());
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation_from_bottom);
        gridStatsView.setAdapter(adapter);
        gridStatsView.setLayoutManager(layoutManager);
        gridStatsView.setLayoutAnimation(controller);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        UserManager userManager = App.getUserManager();
        userManager.get(userId).onSuccess(user -> {
            if (userLoadedListener != null) {
                userLoadedListener.onLoaded(user);
            }

            PartialProfileFragment.this.user = user;
            adapter.setUser(user);

            getActivity().runOnUiThread(() -> {
                username.setText(user.getUsername());
                description.setText(user.getBio());
            });

            if (!Strings.isNullOrEmpty(user.getProfileImageId())) {
                userManager.getProfileImage(user)
                        .onSuccess(image -> getActivity().runOnUiThread(() -> profileImage.setImageBitmap(image)))
                        .onFailedOrError(result -> UiHelper.toastDataResult(getContext(), result));
            }

            userManager.getStats(user).onSuccess(stats -> {
                getActivity().runOnUiThread(() -> {
                    adapter.setStatsList(stats);
                    if (isFirstTime) {
                        gridStatsView.scheduleLayoutAnimation();
                        isFirstTime = false;
                    }
                });
            });
        });
    }

    public void setUserLoadedListener(OnUserLoadedListener userLoadedListener) {
        this.userLoadedListener = userLoadedListener;
    }

    public int getUserId() {
        return userId;
    }

    public User getUser() {
        return user;
    }

    @FunctionalInterface
    interface OnUserLoadedListener {
        void onLoaded(User user);
    }
}
