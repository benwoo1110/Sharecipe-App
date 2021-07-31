package sg.edu.np.mad.Sharecipe.ui.view.information;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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
        TextView displayDesc = view.findViewById(R.id.displayDescription);
        ImageView enlargedImage = view.findViewById(R.id.view_enlargedimage);
        RecyclerView recyclerView = view.findViewById(R.id.viewImages_recyclerView);

        ViewImagesAdapter adapter = new ViewImagesAdapter(getActivity(), enlargedImage, view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation_fall_down);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setLayoutAnimation(controller);

        App.getRecipeManager().getImages(recipe).onSuccess(bitmaps -> {
            getActivity().runOnUiThread(() -> {
                adapter.setBitmapList(bitmaps);
                recyclerView.scheduleLayoutAnimation();
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

        displayDifficulty.setText(recipe.getDifficulty() > 0 ? "Difficulty: " + String.valueOf(recipe.getDifficulty()) + "/5" : "nil");

        App.getUserManager().get(recipe.getUserId()).onSuccess(user -> {
            getActivity().runOnUiThread(() -> displayAuthor.setText("By " + user.getUsername()));
        });

        return view;
    }
}
