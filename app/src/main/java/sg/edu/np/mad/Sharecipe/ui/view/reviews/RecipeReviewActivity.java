package sg.edu.np.mad.Sharecipe.ui.view.reviews;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import sg.edu.np.mad.Sharecipe.data.AccountManager;
import sg.edu.np.mad.Sharecipe.models.Recipe;
import sg.edu.np.mad.Sharecipe.models.RecipeReview;
import sg.edu.np.mad.Sharecipe.ui.App;
import sg.edu.np.mad.Sharecipe.ui.common.AfterTextChangedWatcher;
import sg.edu.np.mad.Sharecipe.ui.common.DynamicFocusAppCompatActivity;
import sg.edu.np.mad.Sharecipe.ui.common.OnSingleClickListener;

public class RecipeReviewActivity extends DynamicFocusAppCompatActivity {

    private List<RecipeReview> recipeReviews;
    private RecipeReview newReview;
    private Button submitReview;
    private Recipe recipe;
    private RecyclerView reviewsRecyclerView;
    private TextView noReviewsMessage;
    private TextView reviewsNumber;
    private TextInputEditText inputReview;
    private RatingBar inputRating;
    private RecipeReviewAdapter adapter;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_review);

        Intent data = getIntent();
        recipe = (Recipe) data.getSerializableExtra(IntentKeys.RECIPE_REVIEW);
        newReview = new RecipeReview();
        recipeReviews = new ArrayList<>();

        inputRating = findViewById(R.id.inputRating);
        inputReview = findViewById(R.id.inputReview);
        reviewsNumber = findViewById(R.id.reviewsNumber);
        submitReview = findViewById(R.id.sendReview);
        reviewsRecyclerView = findViewById(R.id.recyclerview_reviews);
        noReviewsMessage = findViewById(R.id.displayNoReviews);

        inputRating.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> newReview.setRating(Math.round(inputRating.getRating())));
        inputReview.addTextChangedListener((AfterTextChangedWatcher) s -> newReview.setComment(inputReview.getText().toString()));

        submitReview.setOnClickListener((OnSingleClickListener) v -> {
            if (newReview.getRating() == 0) {
                Toast.makeText(RecipeReviewActivity.this, "Please rate this recipe between 1 to 5 stars.", Toast.LENGTH_SHORT).show();
                submitReview.setEnabled(true);
                return;
            } else if (Strings.isNullOrEmpty(newReview.getComment())) {
                Toast.makeText(RecipeReviewActivity.this, "Please leave a comment for this recipe", Toast.LENGTH_SHORT).show();
                submitReview.setEnabled(true);
                return;
            }
            checkSubmit();
        });

        adapter = new RecipeReviewAdapter(recipeReviews);
        LinearLayoutManager cLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        reviewsRecyclerView.setAdapter(adapter);
        reviewsRecyclerView.setLayoutManager(cLayoutManager);

        App.getRecipeManager().getReviews(recipe).onSuccess(recipeReviewList -> {
            int accountId = App.getAccountManager().getAccount().getUserId();
            for (RecipeReview recipeReview : recipeReviewList) {
                if (recipeReview.getUserId() == accountId) {
                    runOnUiThread(() -> {
                        newReview = recipeReview;
                        setAsUpdateRecipe();
                    });
                    break;
                }
            }
            runOnUiThread(() -> {
                adapter.setReviews(recipeReviewList);
                updateReviewShowing();
                submitReview.setEnabled(true);
            });
        }).thenAccept(System.out::println);
    }

    private void checkSubmit() {
        new AlertDialog.Builder(this, R.style.AlertDialogCustom)
                .setTitle("Submit review")
                .setMessage("Are you sure you want to submit this review?")
                .setCancelable(false)
                .setPositiveButton("Yes", ((dialog, which) -> {
                    App.getRecipeManager().accountAddReview(recipe, newReview).onSuccess(review -> {
                        newReview = review;
                        int index = recipeReviews.indexOf(review);
                        if (index != -1) {
                            recipeReviews.set(index, review);
                        } else {
                            recipeReviews.add(review);
                        }
                        runOnUiThread(() -> {
                            Toast.makeText(RecipeReviewActivity.this, "Review successfully submitted.", Toast.LENGTH_SHORT).show();
                            adapter.notifyDataSetChanged();
                            setAsUpdateRecipe();
                            updateReviewShowing();
                            submitReview.setEnabled(true);
                        });
                    });
                }))
                .setNegativeButton("Cancel", ((dialog, which) -> {
                    submitReview.setEnabled(true);
                }))
                .show();
    }

    @SuppressLint("SetTextI18n")
    private void setAsUpdateRecipe() {
        inputRating.setRating(newReview.getRating());
        inputReview.setText(newReview.getComment());
        submitReview.setText("Update");
    }

    @SuppressLint("SetTextI18n")
    private void updateReviewShowing() {
        if (recipeReviews.size() <= 0) {
            reviewsRecyclerView.setVisibility(View.GONE);
            noReviewsMessage.setVisibility(View.VISIBLE);
        } else {
            reviewsRecyclerView.setVisibility(View.VISIBLE);
            noReviewsMessage.setVisibility(View.GONE);
        }

        reviewsNumber.setText("(" + recipeReviews.size() + ")");
    }
}
