package sg.edu.np.mad.Sharecipe.ui.main.recipe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.contants.IntentKeys;

public class UserRecipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_recipe);

        Intent data = getIntent();
        int userId = data.getIntExtra(IntentKeys.USER_ID, -1);
        boolean showLiked = data.getBooleanExtra(IntentKeys.RECIPE_SHOW_LIKED, false);

        if (userId == -1) {
            Toast.makeText(UserRecipeActivity.this, "Invalid user.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, new RecipeFragment(userId, showLiked))
                .commit();
    }
}