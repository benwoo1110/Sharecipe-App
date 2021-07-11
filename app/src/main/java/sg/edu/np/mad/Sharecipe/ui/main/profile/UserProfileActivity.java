package sg.edu.np.mad.Sharecipe.ui.main.profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.data.UserManager;
import sg.edu.np.mad.Sharecipe.ui.common.DynamicFocusAppCompatActivity;

public class UserProfileActivity extends DynamicFocusAppCompatActivity {

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

        UserManager.getInstance(UserProfileActivity.this).get(userid).onSuccess(user -> {
            runOnUiThread(() -> {
                username.setText(user.getUsername());
                description.setText(user.getBio());
            });
        });

        UserManager.getInstance(UserProfileActivity.this).getProfileImage(userid)
                .onSuccess(image -> runOnUiThread(() -> profileImage.setImageBitmap(image)))
                .onFailed(reason -> runOnUiThread(() -> Toast.makeText(UserProfileActivity.this, reason.getMessage(), Toast.LENGTH_SHORT).show()))
                .onError(Throwable::printStackTrace);
    }
}