package sg.edu.np.mad.Sharecipe.ui.main.profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.data.UserManager;

public class UserProfileFragment extends Fragment {

    public UserProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        int userid;
        Button follow = view.findViewById(R.id.follow);
        ImageView profileImage = view.findViewById(R.id.profileImage);
        TextView username = view.findViewById(R.id.username);
        TextView description = view.findViewById(R.id.description);
        TextView following = view.findViewById(R.id.following);
        TextView followers = view.findViewById(R.id.followers);

        Intent receivedData = getActivity().getIntent();
        userid = receivedData.getIntExtra("userId",0);

        UserManager.getInstance(getContext()).get(userid).onSuccess(user -> {
            username.setText(user.getUsername());
            description.setText(user.getBio());
        });

        return view;
    }
}