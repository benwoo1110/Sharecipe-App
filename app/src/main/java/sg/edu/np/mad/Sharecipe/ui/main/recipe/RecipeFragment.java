package sg.edu.np.mad.Sharecipe.ui.main.recipe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.contants.IntentKeys;
import sg.edu.np.mad.Sharecipe.models.Recipe;
import sg.edu.np.mad.Sharecipe.models.User;
import sg.edu.np.mad.Sharecipe.ui.App;
import sg.edu.np.mad.Sharecipe.ui.common.UiHelper;
import sg.edu.np.mad.Sharecipe.ui.create.RecipeCreateActivity;
import sg.edu.np.mad.Sharecipe.ui.main.profile.EditPasswordActivity;

public class RecipeFragment extends Fragment {

    public static int LAUNCH_RECIPE_CREATION = 50;

    private final int userId;
    private final boolean showLiked;
    private final boolean isAccountUser;

    private RecipeAdapter recipeAdapter;
    private ShimmerFrameLayout shimmerFrameLayout;
    private RecyclerView recipeRecyclerView;
    private SwipeRefreshLayout myRecipeRefresh;

    private User user;
    private TextView noRecipeMessage;

    public RecipeFragment() {
        userId = App.getAccountManager().getAccount().getUserId();
        showLiked = false;
        isAccountUser = true;
    }

    public RecipeFragment(int userId, boolean showLiked) {
        this.userId = userId;
        this.showLiked = showLiked;
        this.isAccountUser = App.getAccountManager().getAccount().getUserId() == userId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);

        Toolbar recipeToolbar = view.findViewById(R.id.recipeToolbar);
        FloatingActionButton addRecipe = view.findViewById(R.id.button_create_recipe);
        noRecipeMessage = view.findViewById(R.id.noRecipeMessage);
        shimmerFrameLayout = view.findViewById(R.id.recipeShimmerLayout);
        recipeRecyclerView = view.findViewById(R.id.myRecipeRecyclerView);
        myRecipeRefresh = view.findViewById(R.id.myRecipeRefresh);

        recipeAdapter = new RecipeAdapter(new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation_from_bottom);
        recipeRecyclerView.setLayoutAnimation(controller);
        recipeRecyclerView.setAdapter(recipeAdapter);
        recipeRecyclerView.setLayoutManager(layoutManager);

        if (!showLiked && isAccountUser) {

            recipeToolbar.inflateMenu(R.menu.recipe_like_menu);
            recipeToolbar.setOnMenuItemClickListener(item -> {
                if (item.getItemId() != R.id.liked_recipe_item) {
                    return false;
                }

                Intent intent = new Intent(getContext(), UserRecipeActivity.class);
                intent.putExtra(IntentKeys.USER_ID, userId);
                intent.putExtra(IntentKeys.RECIPE_SHOW_LIKED, true);
                startActivity(intent);
                return true;
            });

            addRecipe.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), RecipeCreateActivity.class);
                Recipe recipe = new Recipe();
                intent.putExtra(IntentKeys.RECIPE_EDIT, recipe);
                startActivityForResult(intent, LAUNCH_RECIPE_CREATION);
            });

        } else {
            addRecipe.setVisibility(View.GONE);
        }

        App.getUserManager().get(userId).onSuccess(user -> {
            RecipeFragment.this.user = user;
            getActivity().runOnUiThread(() -> {
                recipeToolbar.setTitle(user.getUsername() + "'s " + (showLiked ? "Favourites" : "Recipes"));
                myRecipeRefresh.setOnRefreshListener(this::loadRecipe);
                loadRecipe();
            });
        });

        return view;
    }

    private void loadRecipe() {
        noRecipeMessage.setVisibility(View.GONE);
        recipeRecyclerView.setVisibility(View.GONE);
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();

        (showLiked ? App.getRecipeManager().getForUserLikes(user) : App.getRecipeManager().getAllForUser(userId)).onSuccess(recipes -> {
            getActivity().runOnUiThread(() -> {
                recipeAdapter.setRecipeList(recipes);
                recipeRecyclerView.scheduleLayoutAnimation();
                recipeRecyclerView.setVisibility(View.VISIBLE);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                myRecipeRefresh.setRefreshing(false);
                if (recipes.size() <= 0) {
                    String text =  isAccountUser ? "You" : user.getUsername();
                    text += showLiked ? " have not liked any recipe." : " have not created any recipe.";
                    noRecipeMessage.setText(text);
                    noRecipeMessage.setVisibility(View.VISIBLE);
                }
            });
        }).onFailedOrError(result -> UiHelper.toastDataResult(getContext(), result));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != LAUNCH_RECIPE_CREATION || resultCode != Activity.RESULT_OK || data == null) {
            return;
        }

        Recipe recipe = (Recipe) data.getSerializableExtra(IntentKeys.RECIPE_SAVE);
        recipeAdapter.addRecipe(recipe);
        App.getRecipeManager().getIcon(recipe)
                .onSuccess(recipe::setIconEE)
                .thenAccept(result -> getActivity().runOnUiThread(() ->  recipeAdapter.notifyItemChanged(recipeAdapter.getRecipeList().size())));
    }
}
