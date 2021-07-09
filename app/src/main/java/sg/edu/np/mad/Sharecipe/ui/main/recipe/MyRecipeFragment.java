package sg.edu.np.mad.Sharecipe.ui.main.recipe;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

import java9.util.concurrent.CompletableFuture;
import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.data.RecipeManager;
import sg.edu.np.mad.Sharecipe.models.Recipe;

public class MyRecipeFragment extends Fragment {

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

        ShimmerFrameLayout shimmerFrameLayout = view.findViewById(R.id.recipeShimmerLayout);
        RecyclerView recipeRecyclerView = view.findViewById(R.id.myRecipeRecyclerView);

        shimmerFrameLayout.startShimmer();

        RecipeAdapter adapter = new RecipeAdapter(new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation_fall_down);
        recipeRecyclerView.setLayoutAnimation(controller);
        recipeRecyclerView.setAdapter(adapter);
        recipeRecyclerView.setLayoutManager(layoutManager);

        RecipeManager recipeManager = RecipeManager.getInstance(getContext());
        recipeManager.getAccountRecipe()
                .onSuccess(recipes -> {
                    CompletableFuture<?>[] completableFutures = new CompletableFuture[recipes.size()];
                    for (int i = 0, recipesSize = recipes.size(); i < recipesSize; i++) {
                        Recipe recipe = recipes.get(i);
                        completableFutures[i] = recipeManager.getIcon(recipe).onSuccess(recipe::setIcon).onFailed(System.out::println).onError(Throwable::printStackTrace);
                    }
                    CompletableFuture.allOf(completableFutures).thenAccept(aVoid -> {
                        getActivity().runOnUiThread(() -> {
                            adapter.setRecipeList(recipes);
                            recipeRecyclerView.scheduleLayoutAnimation();
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.setVisibility(View.GONE);
                        });
                    });
                })
                .onFailed(reason -> getActivity().runOnUiThread(() -> Toast.makeText(getContext(), reason.getMessage(), Toast.LENGTH_SHORT).show()))
                .onError(Throwable::printStackTrace);

        return view;
    }
}