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
        TextView following = findViewById(R.id.following);
        TextView followers = findViewById(R.id.followers);

        Intent receivedData = getIntent();
        int userid = receivedData.getIntExtra("userId",0);

        UserManager userManager = App.getUserManager();

        userManager.get(userid).onSuccess(user -> {
            runOnUiThread(() -> {
                username.setText(user.getUsername());
                description.setText(user.getBio());
                userManager.getFollows(user).onSuccess(userFollows -> {
                    followers.setText(userFollows.size());
                });
            });
        });

        userManager.getProfileImage(userid)
                .onSuccess(image -> runOnUiThread(() -> profileImage.setImageBitmap(image)))
                .onFailed(reason -> runOnUiThread(() -> Toast.makeText(UserProfileActivity.this, reason.getMessage(), Toast.LENGTH_SHORT).show()))
                .onError(Throwable::printStackTrace);

        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userManager.get(userid).onSuccess(user -> {
                    userManager.accountFollowUser(user);
                });
            }
        });
    }
}