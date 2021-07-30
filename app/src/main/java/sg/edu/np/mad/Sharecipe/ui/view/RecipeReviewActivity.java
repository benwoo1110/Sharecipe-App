package sg.edu.np.mad.Sharecipe.ui.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Rating;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.contants.IntentKeys;
import sg.edu.np.mad.Sharecipe.models.Recipe;

public class RecipeReviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_review);

        Intent review = getIntent();
        Recipe recipe = (Recipe) review.getSerializableExtra(IntentKeys.RECIPE_REVIEW);

        TextView labelReview = findViewById(R.id.labelReview);
        RatingBar inputRating = findViewById(R.id.inputRating);
        TextInputEditText inputReview = findViewById(R.id.inputReview);
        TextView reviewsNumber = findViewById(R.id.reviewsNumber);
        RecyclerView recyclerView = findViewById(R.id.recyclerview_reviews);

        RecipeReviewAdapter adapter = new RecipeReviewAdapter(recipe.getReviews());
        LinearLayoutManager cLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);



    }
}