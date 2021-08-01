package sg.edu.np.mad.Sharecipe.ui.view.reviews;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.List;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.contants.IntentKeys;
import sg.edu.np.mad.Sharecipe.models.Recipe;
import sg.edu.np.mad.Sharecipe.models.RecipeReview;
import sg.edu.np.mad.Sharecipe.ui.App;
import sg.edu.np.mad.Sharecipe.ui.common.AfterTextChangedWatcher;
import sg.edu.np.mad.Sharecipe.ui.common.DynamicFocusAppCompatActivity;

public class RecipeReviewActivity extends DynamicFocusAppCompatActivity {

    private List<RecipeReview> recipeReviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_review);

        Intent review = getIntent();
        Recipe recipe = (Recipe) review.getSerializableExtra(IntentKeys.RECIPE_REVIEW);
        recipeReviews = recipe.getReviews();
        if (recipeReviews == null) {
            recipeReviews = new ArrayList<>();
        }

        RecipeReview newReview = new RecipeReview();

        TextView labelReview = findViewById(R.id.labelReview);
        RatingBar inputRating = findViewById(R.id.inputRating);
        TextInputEditText inputReview = findViewById(R.id.inputReview);
        TextView reviewsNumber = findViewById(R.id.reviewsNumber);
        Button submitReview = findViewById(R.id.sendReview);
        RecyclerView recyclerView = findViewById(R.id.recyclerview_reviews);

        RecipeReviewAdapter adapter = new RecipeReviewAdapter(recipeReviews);
        LinearLayoutManager cLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(cLayoutManager);

        labelReview.setText("Rate " + recipe.getName());
        inputRating.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> newReview.setRating(Math.round(inputRating.getRating())));
        inputReview.addTextChangedListener((AfterTextChangedWatcher) s -> newReview.setComment(inputReview.toString()));

        if (recipe.getReviews() != null) {
            reviewsNumber.setText("(" + String.valueOf(recipeReviews.size()) + ")");
        }
        else {
            reviewsNumber.setText("(0)");
        }

        submitReview.setOnClickListener(v -> {
            if (newReview.getRating() == 0) {
                Toast.makeText(RecipeReviewActivity.this, "Please rate this recipe 1 to 5 stars", Toast.LENGTH_SHORT);
            }
            else if (Strings.isNullOrEmpty(newReview.getComment())) {
                Toast.makeText(RecipeReviewActivity.this, "Please leave a comment for this recipe", Toast.LENGTH_SHORT);
            }
            else {
                App.getUserManager().getAccountUser().onSuccess(user -> newReview.setUser(user));
                App.getUserManager().getAccountUser().onSuccess(user -> newReview.setUsername(user.getUsername()));
                checkSubmit(newReview, recipe);
            }
        });

    }

    private void checkSubmit(RecipeReview newReview, Recipe recipe) {
        new AlertDialog.Builder(this, R.style.AlertDialogCustom)
                .setTitle("Submit review")
                .setMessage("Are you sure you want to submit this review?")
                .setPositiveButton("Yes", ((dialog, which) -> saveReview(newReview, recipe)))
                .setNegativeButton("Cancel", ((dialog, which) -> finish()))
                .show();
    }

    private void saveReview(RecipeReview review, Recipe recipe) {
        recipe.getReviews().add(review);
        finish();
    }
}