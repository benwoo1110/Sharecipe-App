package sg.edu.np.mad.Sharecipe.ui.main.profile.stats;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import sg.edu.np.mad.Sharecipe.contants.IntentKeys;
import sg.edu.np.mad.Sharecipe.models.User;
import sg.edu.np.mad.Sharecipe.ui.main.recipe.RecipeListActivity;
import sg.edu.np.mad.Sharecipe.ui.main.user.UserListActivity;
import sg.edu.np.mad.Sharecipe.ui.main.user.UsersType;

public class StatsActions {

    public static final StatsAction DEFAULT_ACTION = new StatsAction() {
        @NotNull
        @Override
        public StatsType getType() {
            return StatsType.DEFAULT;
        }

        @Override
        public void onClick(@NotNull Context context, @NotNull User user) {
            // Do nothing
        }
    };

    public static final StatsAction FOLLOW_ACTION = new StatsAction() {
        @NotNull
        @Override
        public StatsType getType() {
            return StatsType.FOLLOW;
        }

        @Override
        public void onClick(@NotNull Context context, @NotNull User user) {
            Intent intent = new Intent(context, UserListActivity.class);
            intent.putExtra(IntentKeys.USER, user);
            intent.putExtra(IntentKeys.USER_TYPE, UsersType.FOLLOW);
            context.startActivity(intent);
        }
    };

    public static final StatsAction FOLLOWER_ACTION = new StatsAction() {
        @NotNull
        @Override
        public StatsType getType() {
            return StatsType.FOLLOWER;
        }

        @Override
        public void onClick(@NotNull Context context, @NotNull User user) {
            Intent intent = new Intent(context, UserListActivity.class);
            intent.putExtra(IntentKeys.USER, user);
            intent.putExtra(IntentKeys.USER_TYPE, UsersType.FOLLOWER);
            context.startActivity(intent);
        }
    };

    public static final StatsAction LIKED_RECIPE_ACTION = new StatsAction() {
        @NotNull
        @Override
        public StatsType getType() {
            return StatsType.LIKED_RECIPE;
        }

        @Override
        public void onClick(@NotNull Context context, @NotNull User user) {
            Intent intent = new Intent(context, RecipeListActivity.class);
            intent.putExtra(IntentKeys.USER_ID, user.getUserId());
            intent.putExtra(IntentKeys.RECIPE_SHOW_LIKED, true);
            context.startActivity(intent);
        }
    };

    public static final StatsAction USER_RECIPE_ACTION = new StatsAction() {
        @NotNull
        @Override
        public StatsType getType() {
            return StatsType.USER_RECIPE;
        }

        @Override
        public void onClick(@NotNull Context context, @NotNull User user) {
            Intent intent = new Intent(context, RecipeListActivity.class);
            intent.putExtra(IntentKeys.USER_ID, user.getUserId());
            context.startActivity(intent);
        }
    };

    private static final Map<StatsType, StatsAction> actionMap = new HashMap<>();

    public static void addAction(@NonNull StatsAction action) {
        actionMap.put(action.getType(), action);
    }

    @NonNull
    public static StatsAction forType(StatsType type) {
        StatsAction action = actionMap.get(type);
        if (action != null) {
            return action;
        } else {
            return DEFAULT_ACTION;
        }
    }

    static {
        addAction(DEFAULT_ACTION);
        addAction(FOLLOW_ACTION);
        addAction(FOLLOWER_ACTION);
        addAction(LIKED_RECIPE_ACTION);
        addAction(USER_RECIPE_ACTION);
    }
}
