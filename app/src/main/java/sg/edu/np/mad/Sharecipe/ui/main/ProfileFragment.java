package sg.edu.np.mad.Sharecipe.ui.main;

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
import sg.edu.np.mad.Sharecipe.data.AccountManager;
import sg.edu.np.mad.Sharecipe.data.UserManager;
import sg.edu.np.mad.Sharecipe.ui.LoginActivity;

public class ProfileFragment extends Fragment {

    private Button editButton;
    private Button logoutButton;
    private TextView username;
    private TextView description;
    private ImageView profileImage;

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

        editButton = view.findViewById(R.id.editUserinfo);
        logoutButton = view.findViewById(R.id.buttonLogout);
        username = view.findViewById(R.id.username);
        description = view.findViewById(R.id.description);
        TextView following = view.findViewById(R.id.following);
        TextView followers = view.findViewById(R.id.followers);
        profileImage = view.findViewById(R.id.profileImage);

        UserManager.getInstance(getContext()).getAccountUser().onSuccess(user -> {
            getActivity().runOnUiThread(() -> {
                username.setText(user.getUsername());
                description.setText(user.getBio());
            });
        });

        UserManager.getInstance(getContext()).getProfileImage(AccountManager.getInstance(getContext()).getAccount().getUserId())
                .onSuccess(image -> getActivity().runOnUiThread(() -> profileImage.setImageBitmap(image)))
                .onFailed(reason -> getActivity().runOnUiThread(() -> Toast.makeText(getContext(), reason.getMessage(), Toast.LENGTH_SHORT).show()))
                .onError(Throwable::printStackTrace);

        logoutButton.setOnClickListener(v -> {
            AccountManager.getInstance(getContext()).logout()
                    .onSuccess(ignore -> {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    })
                    .onFailed(reason -> getActivity().runOnUiThread(() -> Toast.makeText(getContext(), reason.getMessage(), Toast.LENGTH_SHORT).show()))
                    .onError(Throwable::printStackTrace);
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                ((MainActivity) getActivity()).startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        UserManager.getInstance(getContext()).getAccountUser().onSuccess(user -> {
            getActivity().runOnUiThread(() -> {
                username.setText(user.getUsername());
                description.setText(user.getBio());
            });
        });

        UserManager.getInstance(getContext()).getProfileImage(AccountManager.getInstance(getContext()).getAccount().getUserId())
                .onSuccess(image -> getActivity().runOnUiThread(() -> profileImage.setImageBitmap(image)))
                .onFailed(reason -> getActivity().runOnUiThread(() -> Toast.makeText(getContext(), reason.getMessage(), Toast.LENGTH_SHORT).show()))
                .onError(Throwable::printStackTrace);

    }



}
