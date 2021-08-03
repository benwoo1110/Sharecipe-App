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

import java.util.ArrayList;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.contants.IntentKeys;
import sg.edu.np.mad.Sharecipe.data.UserManager;
import sg.edu.np.mad.Sharecipe.models.User;
import sg.edu.np.mad.Sharecipe.ui.App;
import sg.edu.np.mad.Sharecipe.ui.common.UiHelper;
import sg.edu.np.mad.Sharecipe.ui.main.recipe.UserRecipeActivity;

public class UserProfileActivity extends AppCompatActivity {

    private User user;
    private boolean isFollowing = false;
    private Button follow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        ImageView profileImage = findViewById(R.id.profileImage);
        RecyclerView gridStatsView = findViewById(R.id.statsRecyclerView);
        TextView username = findViewById(R.id.username);
        TextView description = findViewById(R.id.description);
        Button viewRecipe = findViewById(R.id.viewUserRecipesButton);
        follow = findViewById(R.id.follow);

        // Setup stats grid
        StatsAdapter adapter = new StatsAdapter(new ArrayList<>());
        GridLayoutManager layoutManager = new GridLayoutManager(UserProfileActivity.this, 2);
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(UserProfileActivity.this, R.anim.layout_animation_from_bottom);
        gridStatsView.setAdapter(adapter);
        gridStatsView.setLayoutManager(layoutManager);
        gridStatsView.setLayoutAnimation(controller);

        // Grab the target user id to load
        Intent receivedData = getIntent();
        int userId = receivedData.getIntExtra(IntentKeys.USER_ID, 0);

        // Load the data from web
        UserManager userManager = App.getUserManager();
        userManager.get(userId).onSuccess(resultUser -> {
            user = resultUser;

            runOnUiThread(() -> {
                username.setText(user.getUsername());
                description.setText(user.getBio());
            });

            userManager.checkIfAccountFollow(user).onSuccess(state -> {
                isFollowing = state.getState();
                updateFollowButton();
            });

            userManager.getProfileImage(user)
                    .onSuccess(image -> runOnUiThread(() -> profileImage.setImageBitmap(image)))
                    .onFailedOrError(result -> UiHelper.toastDataResult(UserProfileActivity.this, result));

            userManager.getStats(user).onSuccess(stats -> {
                UserProfileActivity.this.runOnUiThread(() -> {
                    adapter.setStatsList(stats);
                    gridStatsView.scheduleLayoutAnimation();
                });
            });
        });

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
                userManager.accountUnfollowUser(user).onSuccess(aVoid -> {
                    isFollowing = false;
                    updateFollowButton();
                });
            } else {
                userManager.accountFollowUser(user).onSuccess(aVoid -> {
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