package sg.edu.np.mad.Sharecipe.ui.main.profile;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.data.UserManager;
import sg.edu.np.mad.Sharecipe.models.UserStats;
import sg.edu.np.mad.Sharecipe.ui.App;
import sg.edu.np.mad.Sharecipe.ui.LoginActivity;
import sg.edu.np.mad.Sharecipe.ui.common.OnSingleClickListener;

public class ProfileFragment extends Fragment {

    private TextView username;
    private TextView description;
    private ImageView profileImage;

    private UserManager userManager;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        username = view.findViewById(R.id.username);
        description = view.findViewById(R.id.description);
        profileImage = view.findViewById(R.id.profileImage);
        Button editProfileButton = view.findViewById(R.id.editUserinfo);
        Button editPasswordButton = view.findViewById(R.id.passwordButton);
        Button logoutButton = view.findViewById(R.id.buttonLogout);
        Button deleteButton = view.findViewById(R.id.deletaAccountButton);
        RecyclerView gridStatsView = view.findViewById(R.id.statsRecyclerView);

        userManager = App.getUserManager();
        userManager.getAccountUser().onSuccess(user -> {
            getActivity().runOnUiThread(() -> {
                username.setText(user.getUsername());
                description.setText(user.getBio());
                userManager.getFollows(user).onSuccess(userFollows -> {
                    // getActivity().runOnUiThread(() -> followingNo.setText(String.valueOf(userFollows.size())));
                });
                userManager.getFollowers(user).onSuccess(userFollows -> {
                    // getActivity().runOnUiThread(() -> followersNo.setText(String.valueOf(userFollows.size())));
                });
            });
            userManager.getProfileImage(user)
                    .onSuccess(image -> getActivity().runOnUiThread(() -> profileImage.setImageBitmap(image)))
                    .onFailed(reason -> getActivity().runOnUiThread(() -> Toast.makeText(getContext(), reason.getMessage(), Toast.LENGTH_SHORT).show()))
                    .onError(Throwable::printStackTrace);
        });

        editProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            getActivity().startActivity(intent);
        });

        editPasswordButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditPasswordActivity.class);
            getActivity().startActivity(intent);
        });

        logoutButton.setOnClickListener(v -> {
            App.getAccountManager().logout()
                    .onSuccess(ignore -> {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    })
                    .onFailed(reason -> getActivity().runOnUiThread(() -> Toast.makeText(getContext(), reason.getMessage(), Toast.LENGTH_SHORT).show()))
                    .onError(Throwable::printStackTrace);
        });

        deleteButton.setOnClickListener((OnSingleClickListener) v -> new AlertDialog.Builder(getContext())
                .setTitle("Delete account")
                .setMessage("Are you sure you want to delete account? You cannot undo this action.")
                .setCancelable(false)
                .setPositiveButton("Delete", (dialog, which) -> {
                    App.getAccountManager().delete().onSuccess(aVoid -> {
                        Toast.makeText(getContext(), "Account deleted", Toast.LENGTH_SHORT).show();
                        getActivity().runOnUiThread(() -> {
                            Intent intent = new Intent(getContext(), LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        });
                    });
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    deleteButton.setEnabled(true);
                })
                .show());

        List<UserStats> stats = new ArrayList<UserStats>() {{
            add(new UserStats("Follow", 10));
            add(new UserStats("Follower", 14));
            add(new UserStats("Liked recipes", 6));
            add(new UserStats("Created recipes", 3));
        }};

        StatsAdapter adapter = new StatsAdapter(stats);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        gridStatsView.setAdapter(adapter);
        gridStatsView.setLayoutManager(layoutManager);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        userManager.getAccountUser().onSuccess(user -> {
            getActivity().runOnUiThread(() -> {
                username.setText(user.getUsername());
                description.setText(user.getBio());
            });

            userManager.getProfileImage(user)
                    .onSuccess(image -> getActivity().runOnUiThread(() -> profileImage.setImageBitmap(image)))
                    .onFailed(reason -> getActivity().runOnUiThread(() -> Toast.makeText(getContext(), reason.getMessage(), Toast.LENGTH_SHORT).show()))
                    .onError(Throwable::printStackTrace);
        });
    }
}
