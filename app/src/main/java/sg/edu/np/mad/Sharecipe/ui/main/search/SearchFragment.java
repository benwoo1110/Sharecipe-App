package sg.edu.np.mad.Sharecipe.ui.main.search;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import java9.util.concurrent.CompletableFuture;
import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.data.RecipeManager;
import sg.edu.np.mad.Sharecipe.data.SearchManager;
import sg.edu.np.mad.Sharecipe.data.UserManager;
import sg.edu.np.mad.Sharecipe.models.Recipe;
import sg.edu.np.mad.Sharecipe.models.User;
import sg.edu.np.mad.Sharecipe.ui.common.SectionAdapter;

public class SearchFragment extends Fragment {

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        ShimmerFrameLayout shimmerFrameLayout = view.findViewById(R.id.userShimmerLayout);
        RecyclerView searchResultRecyclerView = view.findViewById(R.id.searchResultView);
        TextInputLayout searchInput = view.findViewById(R.id.textInputSearch);
        TextInputEditText searchText = (TextInputEditText) searchInput.getEditText();

        searchResultRecyclerView.setVisibility(View.GONE);
        shimmerFrameLayout.setVisibility(View.GONE);

        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation_from_bottom);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        DividerItemDecoration divider = new DividerItemDecoration(searchResultRecyclerView.getContext(), layoutManager.getOrientation());

        RecipeResultSectionCreator recipeSection = new RecipeResultSectionCreator("Recipes");
        UserResultSectionCreator userSection = new UserResultSectionCreator("Users");
        SectionAdapter searchSectionAdapter = new SectionAdapter()
                .addSection(recipeSection)
                .addSection(userSection);

        searchResultRecyclerView.setLayoutAnimation(controller);
        searchResultRecyclerView.setAdapter(searchSectionAdapter);
        searchResultRecyclerView.setLayoutManager(layoutManager);
        searchResultRecyclerView.addItemDecoration(divider);

        searchText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId != EditorInfo.IME_ACTION_SEARCH) {
                return false;
            }

            View focus = getActivity().getCurrentFocus();
            if (focus != null) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                focus.clearFocus();
            }

            searchResultRecyclerView.setVisibility(View.GONE);
            shimmerFrameLayout.setVisibility(View.VISIBLE);
            shimmerFrameLayout.startShimmer();

            SearchManager.getInstance(view.getContext())
                    .search(searchText.getText().toString())
                    .onSuccess(searchResult -> {
                        List<Recipe> recipes = searchResult.getRecipes();
                        List<User> users = searchResult.getUsers();
                        CompletableFuture<?>[] completableFutures = new CompletableFuture[recipes.size() + users.size()];
                        int i = 0;
                        for (Recipe recipe : recipes) {
                            completableFutures[i++] = RecipeManager.getInstance(getContext())
                                    .getIcon(recipe)
                                    .onSuccess(recipe::setIcon);
                        }
                        for (User user : users) {
                            completableFutures[i++] = UserManager.getInstance(getContext())
                                    .getProfileImage(user.getUserId())
                                    .onSuccess(user::setProfileImage);
                        }

                        CompletableFuture.allOf(completableFutures).thenAccept(aVoid -> {
                            recipeSection.setRecipeList(searchResult.getRecipes());
                            userSection.setUserList(searchResult.getUsers());
                            getActivity().runOnUiThread(() -> {
                                searchResultRecyclerView.setVisibility(View.VISIBLE);
                                searchSectionAdapter.notifyDataSetChanged();
                                searchResultRecyclerView.scheduleLayoutAnimation();
                                shimmerFrameLayout.stopShimmer();
                                shimmerFrameLayout.setVisibility(View.GONE);
                            });
                        });
                    })
                    .onFailed(reason -> getActivity().runOnUiThread(() -> Toast.makeText(getContext(), reason.getMessage(), Toast.LENGTH_SHORT).show()))
                    .onError(Throwable::printStackTrace);

            return true;
        });

        return view;
    }
}
