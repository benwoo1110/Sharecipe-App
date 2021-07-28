package sg.edu.np.mad.Sharecipe.ui.main.profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.data.AccountManager;
import sg.edu.np.mad.Sharecipe.data.UserManager;
import sg.edu.np.mad.Sharecipe.models.User;
import sg.edu.np.mad.Sharecipe.models.UserFollow;
import sg.edu.np.mad.Sharecipe.ui.App;

public class UserProfileActivity extends AppCompatActivity {

    private User user;
    private boolean isFollowing = false;
    private Button follow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        follow = findViewById(R.id.follow);
        ImageView profileImage = findViewById(R.id.profileImage);
        TextView username = findViewById(R.id.username);
        TextView description = findViewById(R.id.description);
        TextView following = findViewById(R.id.followingNo);
        TextView followers = findViewById(R.id.followerNo);

        // Enabled only after data loaded.
        follow.setEnabled(false);

        // Grab the target user id to load
        Intent receivedData = getIntent();
        int userid = receivedData.getIntExtra("userId", 0);

        // Get required data managers
        UserManager userManager = App.getUserManager();
        AccountManager accountManager = App.getAccountManager();

        // Load the data from web
        userManager.get(userid).onSuccess(resultUser -> {
            user = resultUser;

            runOnUiThread(() -> {
                username.setText(user.getUsername());
                description.setText(user.getBio());
            });

            userManager.getFollows(user).onSuccess(userFollows -> {
                runOnUiThread(() -> following.setText(String.valueOf(userFollows.size())));

                int accountUserId = accountManager.getAccount().getUserId();
                for (UserFollow userFollow : userFollows) {
                    if (userFollow.getUserId() == accountUserId) {
                        isFollowing = true;
                        break;
                    }
                }
                updateFollowButton();
            });

            userManager.getProfileImage(user)
                    .onSuccess(image -> runOnUiThread(() -> profileImage.setImageBitmap(image)))
                    .onFailed(reason -> runOnUiThread(() -> Toast.makeText(UserProfileActivity.this, reason.getMessage(), Toast.LENGTH_SHORT).show()))
                    .onError(Throwable::printStackTrace);
        });

        follow.setOnClickListener(v -> {
            isFollowing ^= true;
            follow.setEnabled(false);
            if (isFollowing) {
                userManager.accountUnfollowUser(user).onSuccess(aVoid -> updateFollowButton());
            } else {
                userManager.accountFollowUser(user).onSuccess(aVoid -> updateFollowButton());
            }
        });
    }

    private void updateFollowButton() {
        runOnUiThread(() -> {
            follow.setText(isFollowing ? "Unfollow" : "Follow");
            follow.setEnabled(true);
        });
    }
}