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
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.data.SearchManager;
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

        RecyclerView searchResultView = view.findViewById(R.id.searchResultView);
        TextInputLayout searchInput = view.findViewById(R.id.textInputSearch);
        TextInputEditText searchText = (TextInputEditText) searchInput.getEditText();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        DividerItemDecoration divider = new DividerItemDecoration(searchResultView.getContext(), layoutManager.getOrientation());

        RecipeResultSectionCreator recipeSection = new RecipeResultSectionCreator("Recipes");
        UserResultSectionCreator userSection = new UserResultSectionCreator("Users");
        SectionAdapter searchSectionAdapter = new SectionAdapter()
                .addSection(recipeSection)
                .addSection(userSection);

        searchResultView.setAdapter(searchSectionAdapter);
        searchResultView.setLayoutManager(layoutManager);
        searchResultView.addItemDecoration(divider);

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

            SearchManager.getInstance(view.getContext())
                    .search(searchText.getText().toString())
                    .onSuccess(searchResult -> {
                        recipeSection.setRecipeList(searchResult.getRecipes());
                        userSection.setUserList(searchResult.getUsers());
                        searchSectionAdapter.notifyDataSetChanged();
                    })
                    .onFailed(reason -> getActivity().runOnUiThread(() -> Toast.makeText(getContext(), reason.getMessage(), Toast.LENGTH_SHORT).show()))
                    .onError(Throwable::printStackTrace);

            return true;
        });

        return view;
    }
}
