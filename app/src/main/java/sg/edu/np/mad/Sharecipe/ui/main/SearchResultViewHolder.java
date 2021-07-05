package sg.edu.np.mad.Sharecipe.ui.main;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.data.RecipeManager;
import sg.edu.np.mad.Sharecipe.models.Recipe;
import sg.edu.np.mad.Sharecipe.models.RecipeStep;

public class SearchResultViewHolder extends RecyclerView.ViewHolder {

    final TextView name;
    final TextView bio;

    int userId;

    public SearchResultViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.textViewName);
        bio = itemView.findViewById(R.id.textViewBio);

        itemView.setOnClickListener(v -> {
            // intent to profile
        });

//        itemView.setOnClickListener(v -> {
//            Recipe newRecipe = new Recipe();
//            newRecipe.setName(name.getText().toString());
//            newRecipe.setDifficulty(10);
//            List<RecipeStep> steps = new ArrayList<RecipeStep>() {{
//                add(new RecipeStep(1, "bah", "boop"));
//                add(new RecipeStep(2, "lah", "mee"));
//            }};
//            newRecipe.setSteps(steps);
//
//            RecipeManager.getInstance(itemView.getContext()).save(newRecipe)
//                    .onSuccess(System.out::println)
//                    .onFailed(System.out::println)
//                    .onError(Throwable::printStackTrace);
//        });
    }
}
