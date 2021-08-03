package sg.edu.np.mad.Sharecipe.ui.main.search;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.ui.App;
import sg.edu.np.mad.Sharecipe.ui.common.SectionAdapter;
import sg.edu.np.mad.Sharecipe.ui.common.UiHelper;
import sg.edu.np.mad.Sharecipe.ui.main.profile.EditPasswordActivity;

public class SearchFragment extends Fragment {

    private TextInputEditText searchText;
    private boolean hasDoneSearch = false;

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
        searchText = (TextInputEditText) searchInput.getEditText();

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

            App.getSearchManager().search(searchText.getText().toString())
                    .onSuccess(searchResult -> {
                        recipeSection.setRecipeList(searchResult.getRecipes());
                        userSection.setUserList(searchResult.getUsers());
                        getActivity().runOnUiThread(() -> {
                            layoutManager.scrollToPosition(0);
                            searchResultRecyclerView.setVisibility(View.VISIBLE);
                            searchSectionAdapter.notifyDataSetChanged();
                            searchResultRecyclerView.scheduleLayoutAnimation();
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.setVisibility(View.GONE);
                        });
                        hasDoneSearch = true;
                    }).onFailedOrError(result -> UiHelper.toastDataResult(getContext(), result));

            return true;
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!hasDoneSearch) {
            searchText.requestFocus();
            Activity activity = getActivity();
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        Activity activity = getActivity();
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View currentFocus = activity.getCurrentFocus();
        if(currentFocus != null && imm.isAcceptingText()) {
            imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
    }
}
