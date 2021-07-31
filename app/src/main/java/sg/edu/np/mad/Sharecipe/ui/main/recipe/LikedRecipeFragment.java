package sg.edu.np.mad.Sharecipe.ui.main.recipe;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.ui.App;
import sg.edu.np.mad.Sharecipe.ui.create.RecipeCreateActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LikedRecipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LikedRecipeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private RecipeAdapter recipeAdapter;
    public static int LAUNCH_RECIPE_CREATION = 50;
    private String mParam2;

    public LikedRecipeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LikedRecipeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LikedRecipeFragment newInstance(String param1, String param2) {
        LikedRecipeFragment fragment = new LikedRecipeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_liked_recipe, container, false);

        Toolbar myRecipeToolbar = view.findViewById(R.id.likedRecipeToolbar);
        FloatingActionButton addRecipe = view.findViewById(R.id.button_create_recipe);
        ShimmerFrameLayout shimmerFrameLayout = view.findViewById(R.id.recipeShimmerLayout);
        RecyclerView recipeRecyclerView = view.findViewById(R.id.likedRecipeRecyclerView);

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

        return view;

    }
}