package sg.edu.np.mad.Sharecipe.ui.main.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.base.Strings;

import java.util.ArrayList;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.contants.IntentKeys;
import sg.edu.np.mad.Sharecipe.data.UserManager;
import sg.edu.np.mad.Sharecipe.models.User;
import sg.edu.np.mad.Sharecipe.ui.App;
import sg.edu.np.mad.Sharecipe.ui.common.UiHelper;
import sg.edu.np.mad.Sharecipe.ui.main.recipe.UserRecipeActivity;

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

        Button viewRecipe = findViewById(R.id.viewUserRecipesButton);
        follow = findViewById(R.id.follow);

        viewRecipe.setOnClickListener(v -> {
            Intent intent = new Intent(UserProfileActivity.this, UserRecipeActivity.class);
            intent.putExtra(IntentKeys.USER_ID, userId);
            startActivity(intent);
        });

        // Enabled only after data loaded.
        follow.setEnabled(false);

        // Toggle follow on click
        follow.setOnClickListener(v -> {
            follow.setEnabled(false);
            if (isFollowing) {
                App.getUserManager().accountUnfollowUser(profileFragment.getUser()).onSuccess(aVoid -> {
                    isFollowing = false;
                    updateFollowButton();
                });
            } else {
                App.getUserManager().accountFollowUser(profileFragment.getUser()).onSuccess(aVoid -> {
                    isFollowing = true;
                    updateFollowButton();
                });
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