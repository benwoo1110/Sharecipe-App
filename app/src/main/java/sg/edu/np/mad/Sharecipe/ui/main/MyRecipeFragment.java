package sg.edu.np.mad.Sharecipe.ui.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.data.RecipeManager;

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
        recipeRecyclerView.setAdapter(adapter);
        recipeRecyclerView.setLayoutManager(layoutManager);

        //TODO
        // Shimmer effect
        // https://howtodoandroid.medium.com/shimmer-effect-for-android-recyclerview-example-a9315b46cdc0
        // https://github.com/facebook/shimmer-android

        RecipeManager.getInstance(getContext()).getAccountRecipe()
                .onSuccess(recipes -> getActivity().runOnUiThread(() -> {
                    adapter.setRecipeList(recipes);
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                }))
                .onFailed(reason -> getActivity().runOnUiThread(() -> Toast.makeText(getContext(), reason.getMessage(), Toast.LENGTH_SHORT).show()))
                .onError(Throwable::printStackTrace);

        return view;
    }
}