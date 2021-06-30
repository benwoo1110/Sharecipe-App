package sg.edu.np.mad.Sharecipe.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.data.AccountManager;
import sg.edu.np.mad.Sharecipe.data.UserManager;

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

        UserManager.getInstance(getContext()).getProfileImage(AccountManager.getInstance(getContext()).getAccount().getUserId())
                .onSuccess(image -> getActivity().runOnUiThread(() -> profileImage.setImageBitmap(image)))
                .onFailed(reason -> getActivity().runOnUiThread(() -> Toast.makeText(getContext(), reason, Toast.LENGTH_SHORT).show()))
                .onError(Throwable::printStackTrace);

        return view;
    }
}