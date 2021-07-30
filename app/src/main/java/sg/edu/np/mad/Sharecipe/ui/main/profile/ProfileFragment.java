package sg.edu.np.mad.Sharecipe.ui.main.profile;

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

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.data.UserManager;
import sg.edu.np.mad.Sharecipe.ui.App;
import sg.edu.np.mad.Sharecipe.ui.LoginActivity;

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

        Button editProfileButton = view.findViewById(R.id.editUserinfo);
        Button editPasswordButton = view.findViewById(R.id.passwordButton);
        Button logoutButton = view.findViewById(R.id.buttonLogout);
        username = view.findViewById(R.id.username);
        description = view.findViewById(R.id.description);
        TextView followingNo = view.findViewById(R.id.followingNo);
        TextView followersNo = view.findViewById(R.id.followerNo);
        profileImage = view.findViewById(R.id.profileImage);

        userManager = App.getUserManager();

        userManager.getAccountUser().onSuccess(user -> {
            getActivity().runOnUiThread(() -> {
                username.setText(user.getUsername());
                description.setText(user.getBio());
                userManager.getFollows(user).onSuccess(userFollows -> {
                    getActivity().runOnUiThread(() -> followingNo.setText(String.valueOf(userFollows.size())));
                });
                userManager.getFollowers(user).onSuccess(userFollows -> {
                    getActivity().runOnUiThread(() -> followersNo.setText(String.valueOf(userFollows.size())));
                });
            });
            userManager.getProfileImage(user)
                    .onSuccess(image -> getActivity().runOnUiThread(() -> profileImage.setImageBitmap(image)))
                    .onFailed(reason -> getActivity().runOnUiThread(() -> Toast.makeText(getContext(), reason.getMessage(), Toast.LENGTH_SHORT).show()))
                    .onError(Throwable::printStackTrace);
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

        editProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            getActivity().startActivity(intent);
        });

        editPasswordButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditPasswordActivity.class);
            getActivity().startActivity(intent);
        });

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
