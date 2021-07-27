package sg.edu.np.mad.Sharecipe.ui.main.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.data.UserManager;
import sg.edu.np.mad.Sharecipe.models.User;
import sg.edu.np.mad.Sharecipe.models.UserFollow;
import sg.edu.np.mad.Sharecipe.ui.App;

public class UserProfileActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);


        Button follow = findViewById(R.id.follow);
        ImageView profileImage = findViewById(R.id.profileImage);
        TextView username = findViewById(R.id.username);
        TextView description = findViewById(R.id.description);
        TextView following = findViewById(R.id.followingNo);
        TextView followers = findViewById(R.id.followerNo);

        Intent receivedData = getIntent();
        int userid = receivedData.getIntExtra("userId", 0);

        UserManager userManager = App.getUserManager();

        userManager.get(userid).onSuccess(user -> {
            runOnUiThread(() -> {
                username.setText(user.getUsername());
                description.setText(user.getBio());
                userManager.getFollows(user).onSuccess(userFollows -> {
                    following.setText(userFollows.size());
                });
                userManager.getFollowers(user).onSuccess(userFollows -> {
                    followers.setText(userFollows.size());
                    for (int i = 0; i < userFollows.size(); i++) {
                        UserFollow userfollow = userFollows.get(i);
                        userManager.getAccountUser().onSuccess(user1 -> {
                            if (userfollow.getFollowId() == user1.getUserId()) {
                                    follow.setText("Unfollow");
                            }
                            else{
                                follow.setText("Follow");
                            }
                        });
                    }
                });
                userManager.getProfileImage(user)
                        .onSuccess(image -> runOnUiThread(() -> profileImage.setImageBitmap(image)))
                        .onFailed(reason -> runOnUiThread(() -> Toast.makeText(UserProfileActivity.this, reason.getMessage(), Toast.LENGTH_SHORT).show()))
                        .onError(Throwable::printStackTrace);
            });
        });

        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (follow.getText() == "Follow"){
                    userManager.get(userid).onSuccess(user -> {
                        userManager.accountFollowUser(user);
                    });
                    follow.setText("Unfollow");
                }
                else if (follow.getText() == "Unfollow"){
                    userManager.get(userid).onSuccess(user -> {
                        userManager.accountUnfollowUser(user);
                    });
                    follow.setText("Follow");
                }
            }
        });
    }
}