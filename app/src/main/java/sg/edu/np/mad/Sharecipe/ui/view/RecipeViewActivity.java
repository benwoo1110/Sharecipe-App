package sg.edu.np.mad.Sharecipe.ui.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.contants.IntentKeys;
import sg.edu.np.mad.Sharecipe.models.PartialRecipe;
import sg.edu.np.mad.Sharecipe.ui.App;
import sg.edu.np.mad.Sharecipe.ui.common.OnTabSelectedListener;
import sg.edu.np.mad.Sharecipe.ui.create.RecipeCreateActivity;
import sg.edu.np.mad.Sharecipe.ui.main.MainActivity;

public class RecipeViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_view);

        Intent getRecipe = getIntent();
        int selectedRecipeId = getRecipe.getIntExtra(IntentKeys.RECIPE_VIEW, 0);

        TabLayout tabLayout = findViewById(R.id.viewRecipeTab);
        ViewPager2 viewpager = findViewById(R.id.view_recipe_viewpager);
        BottomNavigationView bottomNavigation = findViewById(R.id.viewRecipe_navigation);
        bottomNavigation.setSelectedItemId(R.id.invisibleView);

        tabLayout.addTab(tabLayout.newTab().setText("Information"));
        tabLayout.addTab(tabLayout.newTab().setText("Ingredients"));
        tabLayout.addTab(tabLayout.newTab().setText("Steps"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        tabLayout.addOnTabSelectedListener((OnTabSelectedListener) tab -> viewpager.setCurrentItem(tab.getPosition()));

        int userID = App.getAccountManager().getAccount().getUserId();

        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.recipe_review_menu) {
                App.getRecipeManager().get(selectedRecipeId).onSuccess(recipe -> {
                    Intent review = new Intent(RecipeViewActivity.this, RecipeReviewActivity.class);
                    review.putExtra(IntentKeys.RECIPE_REVIEW, recipe);
                    startActivity(review);
                });
                return false;
            } else if (itemId == R.id.recipe_edit_menu) {
                App.getRecipeManager().get(selectedRecipeId).onSuccess(recipe -> {
                    Intent editRecipe = new Intent(RecipeViewActivity.this, RecipeCreateActivity.class);
                    editRecipe.putExtra(IntentKeys.RECIPE_EDIT, recipe);
                    startActivity(editRecipe);
                });

                // TODO: Edit recipe
                return false;
            } else if (itemId == R.id.recipe_like_menu) {
                // TODO: Like recipe
                return false;
            }
            return false;
        });

        App.getRecipeManager().get(selectedRecipeId).onSuccess(recipe -> {
            RecipeViewActivity.this.runOnUiThread(() -> {
                if (recipe.getUserId() != userID) {
                    bottomNavigation.getMenu().findItem(R.id.recipe_edit_menu).setVisible(false);
                }
                RecipeViewAdapter adapter = new RecipeViewAdapter(this, tabLayout.getTabCount(), recipe);
                viewpager.setAdapter(adapter);
            });
        }).onFailed(failed -> {
            Toast.makeText(RecipeViewActivity.this, "Failed to load recipe.", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
