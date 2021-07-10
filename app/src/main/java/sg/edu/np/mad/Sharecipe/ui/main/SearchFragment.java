package sg.edu.np.mad.Sharecipe.ui.main;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.data.UserManager;

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

        SearchResultAdapter searchResultAdapter = new SearchResultAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        DividerItemDecoration divider = new DividerItemDecoration(searchResultView.getContext(), layoutManager.getOrientation());

        searchResultView.setAdapter(searchResultAdapter);
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

            UserManager.getInstance(view.getContext())
                    .search(searchText.getText().toString())
                    .onSuccess(userList -> {
                        searchResultAdapter.setUserList(userList);
                        getActivity().runOnUiThread(searchResultAdapter::notifyDataSetChanged);
                    })
                    .onFailed(reason -> getActivity().runOnUiThread(() -> Toast.makeText(getContext(), reason.getMessage(), Toast.LENGTH_SHORT).show()))
                    .onError(Throwable::printStackTrace);

            return true;
        });

        return view;
    }
}
