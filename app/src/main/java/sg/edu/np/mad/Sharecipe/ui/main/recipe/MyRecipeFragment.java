package sg.edu.np.mad.Sharecipe.ui.main.recipe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
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
import sg.edu.np.mad.Sharecipe.ui.App;
import sg.edu.np.mad.Sharecipe.ui.create.RecipeCreateActivity;

public class MyRecipeFragment extends Fragment {

    public static int LAUNCH_RECIPE_CREATION = 50;

    private RecipeAdapter recipeAdapter;
    private ShimmerFrameLayout shimmerFrameLayout;
    private RecyclerView recipeRecyclerView;
    private SwipeRefreshLayout myRecipeRefresh;

    public MyRecipeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_recipe, container, false);

        Toolbar myRecipeToolbar = view.findViewById(R.id.myRecipeToolbar);
        FloatingActionButton addRecipe = view.findViewById(R.id.button_create_recipe);
        shimmerFrameLayout = view.findViewById(R.id.recipeShimmerLayout);
        recipeRecyclerView = view.findViewById(R.id.myRecipeRecyclerView);
        myRecipeRefresh = view.findViewById(R.id.myRecipeRefresh);

        recipeAdapter = new RecipeAdapter(new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation_from_bottom);
        recipeRecyclerView.setLayoutAnimation(controller);
        recipeRecyclerView.setAdapter(recipeAdapter);
        recipeRecyclerView.setLayoutManager(layoutManager);

        addRecipe.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), RecipeCreateActivity.class);
            Recipe recipe = new Recipe();
            intent.putExtra(IntentKeys.RECIPE_EDIT, recipe);
            startActivityForResult(intent, LAUNCH_RECIPE_CREATION);
        });

        App.getUserManager().getAccountUser().onSuccess(user -> {
            getActivity().runOnUiThread(() -> {
                myRecipeToolbar.setTitle(user.getUsername() + "'s Recipe");
            });
        });

        myRecipeRefresh.setOnRefreshListener(this::loadMyRecipe);

        loadMyRecipe();

        return view;
    }

    private void loadMyRecipe() {
        recipeRecyclerView.setVisibility(View.GONE);
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();

        App.getRecipeManager().getAccountRecipes().onSuccess(recipes -> {
            getActivity().runOnUiThread(() -> {
                recipeAdapter.setRecipeList(recipes);
                recipeRecyclerView.scheduleLayoutAnimation();
                recipeRecyclerView.setVisibility(View.VISIBLE);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                myRecipeRefresh.setRefreshing(false);
            });
        })
        .onFailed(reason -> getActivity().runOnUiThread(() -> Toast.makeText(getContext(), reason.getMessage(), Toast.LENGTH_SHORT).show()))
        .onError(Throwable::printStackTrace);
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
