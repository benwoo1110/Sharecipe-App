package sg.edu.np.mad.Sharecipe.ui.main.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.contants.IntentKeys;
import sg.edu.np.mad.Sharecipe.models.User;
import sg.edu.np.mad.Sharecipe.ui.App;
import sg.edu.np.mad.Sharecipe.ui.common.UiHelper;
import sg.edu.np.mad.Sharecipe.utils.FutureDataResult;

public class UserListActivity extends AppCompatActivity {

    private Toolbar usersToolbar;
    private UserAdapter adapter;
    private UsersType type;
    private User user;
    private RecyclerView usersView;
    private ShimmerFrameLayout shimmerFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        Intent data = getIntent();
        user = (User) data.getSerializableExtra(IntentKeys.USER);
        type = (UsersType) data.getSerializableExtra(IntentKeys.USER_TYPE);

        usersToolbar = findViewById(R.id.usersToolbar);
        usersView = findViewById(R.id.usersRecyclerView);
        shimmerFrameLayout = findViewById(R.id.usersShimmerLayout);

        adapter = new UserAdapter(new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(UserListActivity.this);
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(UserListActivity.this, R.anim.layout_animation_from_bottom);
        usersView.setAdapter(adapter);
        usersView.setLayoutManager(layoutManager);
        usersView.setLayoutAnimation(controller);

        loadUsers();
    }

    private void loadUsers() {
        FutureDataResult<List<User>> futureUsers;
        switch (type) {
            case FOLLOW:
                usersToolbar.setTitle(user.getUsername() + "'s Follows");
                futureUsers = App.getUserManager().getFollows(user);
                break;

            case FOLLOWER:
                usersToolbar.setTitle(user.getUsername() + "'s Followers");
                futureUsers = App.getUserManager().getFollowers(user);
                break;

            default:
                return;
        }

        futureUsers.onSuccess(users -> {
            runOnUiThread(() -> {
                System.out.println(users);
                shimmerFrameLayout.setVisibility(View.GONE);
                adapter.setUserList(users);
                usersView.scheduleLayoutAnimation();
            });
        }).onFailedOrError(result -> UiHelper.toastDataResult(UserListActivity.this, result));
    }
}
