package sg.edu.np.mad.Sharecipe.ui.main.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.transition.TransitionManager;

import com.transitionseverywhere.ChangeText;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.contants.IntentKeys;
import sg.edu.np.mad.Sharecipe.ui.App;

public class UserProfileActivity extends AppCompatActivity {

    private boolean isFollowing = false;
    private Button follow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Grab the target user id to load
        Intent receivedData = getIntent();
        int userId = receivedData.getIntExtra(IntentKeys.USER_ID, -1);
        if (userId == -1) {
            Toast.makeText(UserProfileActivity.this, "Invalid user id.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        PartialProfileFragment profileFragment = new PartialProfileFragment(userId);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, profileFragment)
                .commit();

        follow = findViewById(R.id.follow);

        profileFragment.setUserLoadedListener(user -> {
            App.getUserManager().checkIfAccountFollow(user).onSuccess(state -> {
                isFollowing = state.getState();
                runOnUiThread(() -> {
                    TransitionManager.beginDelayedTransition((ViewGroup) follow.getRootView());
                    follow.setVisibility(View.VISIBLE);
                    updateFollowButton(false);
                });
            });
        });

        // Toggle follow on click
        follow.setOnClickListener(v -> {
            if (isFollowing) {
                App.getUserManager().accountUnfollowUser(profileFragment.getUser()).onSuccess(aVoid -> {
                    isFollowing = false;
                    updateFollowButton(true);
                });
            } else {
                App.getUserManager().accountFollowUser(profileFragment.getUser()).onSuccess(aVoid -> {
                    isFollowing = true;
                    updateFollowButton(true);
                });
            }
        });
    }

    private void updateFollowButton(boolean toast) {
        runOnUiThread(() -> {
            follow.setText(isFollowing ? "Unfollow" : "Follow");
            follow.setEnabled(true);
            if (toast) {
                if (isFollowing) {
                    Toast.makeText(UserProfileActivity.this, "Followed", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UserProfileActivity.this, "Unfollowed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}