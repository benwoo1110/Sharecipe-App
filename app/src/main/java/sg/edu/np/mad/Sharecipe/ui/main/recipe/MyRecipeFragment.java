package sg.edu.np.mad.Sharecipe.ui.main.recipe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.contants.IntentKeys;
import sg.edu.np.mad.Sharecipe.models.Recipe;
import sg.edu.np.mad.Sharecipe.ui.App;
import sg.edu.np.mad.Sharecipe.ui.create.RecipeCreateActivity;
import sg.edu.np.mad.Sharecipe.ui.main.MainActivity;

public class MyRecipeFragment extends Fragment {

    public static int LAUNCH_RECIPE_CREATION = 50;

    private RecipeAdapter recipeAdapter;

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
        ShimmerFrameLayout shimmerFrameLayout = view.findViewById(R.id.recipeShimmerLayout);
        RecyclerView recipeRecyclerView = view.findViewById(R.id.myRecipeRecyclerView);
        Button likedRecipe = view.findViewById(R.id.liked);

        shimmerFrameLayout.startShimmer();

        recipeAdapter = new RecipeAdapter(new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation_from_bottom);
        recipeRecyclerView.setLayoutAnimation(controller);
        recipeRecyclerView.setAdapter(recipeAdapter);
        recipeRecyclerView.setLayoutManager(layoutManager);

        App.getRecipeManager().getAccountRecipes().onSuccess(recipes -> {
            getActivity().runOnUiThread(() -> {
                System.out.println(recipes);
                recipeAdapter.setRecipeList(recipes);
                recipeRecyclerView.scheduleLayoutAnimation();
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
            });
        })
        .onFailed(reason -> getActivity().runOnUiThread(() -> Toast.makeText(getContext(), reason.getMessage(), Toast.LENGTH_SHORT).show()))
        .onError(Throwable::printStackTrace);

        addRecipe.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), RecipeCreateActivity.class);
            startActivityForResult(intent, LAUNCH_RECIPE_CREATION);
        });

        App.getUserManager().getAccountUser().onSuccess(user -> {
            getActivity().runOnUiThread(() -> {
                myRecipeToolbar.setTitle(user.getUsername() + "'s Recipe");
            });
        });

        likedRecipe.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), LikedRecipeFragment.class);
            startActivity(intent);
        });

        return view;
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
