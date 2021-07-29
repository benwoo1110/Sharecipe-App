package sg.edu.np.mad.Sharecipe.ui.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.models.Recipe;
import sg.edu.np.mad.Sharecipe.ui.App;
import sg.edu.np.mad.Sharecipe.utils.FormatUtils;

public class ViewInformationFragment extends Fragment {

    private final Recipe recipe;

    public ViewInformationFragment(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_information, container, false);
        TextView displayName = view.findViewById(R.id.viewRecipeName);
        TextView displayAuthor = view.findViewById(R.id.viewAuthor);
        TextView displayPortion = view.findViewById(R.id.viewPortion);
        TextView displayPrep = view.findViewById(R.id.viewPrep);
        TextView displayDifficulty = view.findViewById(R.id.viewDifficulty);
        TextView displayTotalRating = view.findViewById(R.id.totalRate);
        TextView displayDesc = view.findViewById(R.id.displayDescription);
        TextView labelReview = view.findViewById(R.id.labelReview);
        RatingBar inputRating = view.findViewById(R.id.rateRecipe);
        RecyclerView recyclerView = view.findViewById(R.id.viewImages_recyclerView);

        ViewImagesAdapter adapter = new ViewImagesAdapter();
        LinearLayoutManager cLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(cLayoutManager);

        App.getRecipeManager().getImages(recipe).onSuccess(bitmaps -> {
            getActivity().runOnUiThread(() -> {
                adapter.setBitmapList(bitmaps);
            });
        }).onFailed(System.out::println).onError(Throwable::printStackTrace);

        // Set name of recipe
        displayName.setText(recipe.getName());

        // Set number of portions, display not specified if it is not set
        displayPortion.setText(recipe.getPortion() > 0 ? "Serves " + recipe.getPortion() : "nil");

        // Set preparation time needed, display not specified if it is not set
        displayPrep.setText(recipe.getTotalTimeNeeded().isZero()
                ? "nil"
                : FormatUtils.parseDurationLong(recipe.getTotalTimeNeeded()));

        displayDesc.setText(recipe.getDescription());
        labelReview.setText("Leave a review for " + recipe.getName() + "!");

        return view;
    }
}
