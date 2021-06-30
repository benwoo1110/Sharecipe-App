package sg.edu.np.mad.Sharecipe.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java9.util.function.Consumer;
import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.data.AccountManager;
import sg.edu.np.mad.Sharecipe.data.UserManager;
import sg.edu.np.mad.Sharecipe.models.Account;
import sg.edu.np.mad.Sharecipe.ui.LoginActivity;

public class ProfileFragment extends Fragment {

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

        ImageView profileImage = view.findViewById(R.id.profileImage);
        Button logoutButton = view.findViewById(R.id.buttonLogout);

        UserManager.getInstance(getContext()).getProfileImage(AccountManager.getInstance(getContext()).getAccount().getUserId())
                .onSuccess(image -> getActivity().runOnUiThread(() -> profileImage.setImageBitmap(image)))
                .onFailed(reason -> getActivity().runOnUiThread(() -> Toast.makeText(getContext(), reason, Toast.LENGTH_SHORT).show()))
                .onError(Throwable::printStackTrace);

        logoutButton.setOnClickListener(v -> {
            AccountManager.getInstance(getContext()).logout()
                    .onSuccess(ignore -> {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    })
                    .onFailed(reason -> getActivity().runOnUiThread(() -> Toast.makeText(getContext(), reason, Toast.LENGTH_SHORT).show()))
                    .onError(Throwable::printStackTrace);
        });

        return view;
    }
}
