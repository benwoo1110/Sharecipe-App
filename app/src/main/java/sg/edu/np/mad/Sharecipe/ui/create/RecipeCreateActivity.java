package sg.edu.np.mad.Sharecipe.ui.create;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.contants.IntentKeys;
import sg.edu.np.mad.Sharecipe.models.Recipe;
import sg.edu.np.mad.Sharecipe.ui.App;
import sg.edu.np.mad.Sharecipe.ui.common.OnTabSelectedListener;

// TODO: Finish up bottom menu bar including their actions, add a cross out bar on top to close recipe creation (ask if want to save as draft)
// TODO: Implement option to take in all inputs and save recipe as draft or publish, with input validation (notify users of missing fields)
// TODO: Upon publishing recipe, show user preview of recipe first before finalizing

public class RecipeCreateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_create);

        Recipe recipe = new Recipe();

        TabLayout tabLayout = findViewById(R.id.recipeTab);
        ViewPager2 viewPager = findViewById(R.id.recipe_info_viewpager);
        BottomNavigationView bottomNavigation = findViewById(R.id.recipe_navigation);

        tabLayout.addTab(tabLayout.newTab().setText("Information"));
        tabLayout.addTab(tabLayout.newTab().setText("Ingredients"));
        tabLayout.addTab(tabLayout.newTab().setText("Steps"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        RecipeCreateAdapter adapter = new RecipeCreateAdapter(this, tabLayout.getTabCount(), recipe);
        viewPager.setAdapter(adapter);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        tabLayout.addOnTabSelectedListener((OnTabSelectedListener) tab -> viewPager.setCurrentItem(tab.getPosition()));

        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.recipe_back_menu) {
                Log.v("LOL", "Back");
                finish();
                return false;
            } else if (itemId == R.id.recipe_save_menu) {
                Log.v("LOL", "Save");
                return false;
            } else if (itemId == R.id.recipe_clear_menu) {
                Log.v("LOL", "Clear");
                return false;
            } else if (itemId == R.id.recipe_done_menu) {
                Toast.makeText(RecipeCreateActivity.this, "Saving...", Toast.LENGTH_SHORT).show();
                App.getRecipeManager().create(recipe).onSuccess(createdRecipe -> {
                    App.getRecipeManager().addImages(createdRecipe, adapter.getImageFileList()).thenAccept(result -> {
                        RecipeCreateActivity.this.runOnUiThread(() -> {
                            Toast.makeText(RecipeCreateActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.putExtra(IntentKeys.RECIPE_SAVE, createdRecipe);
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        });
                    });
                }).onFailed(recipeFailed -> {
                    RecipeCreateActivity.this.runOnUiThread(() -> Toast.makeText(RecipeCreateActivity.this, recipeFailed.getMessage(), Toast.LENGTH_SHORT).show());
                });
                Log.v("LOL", "Done");
                return true;
            }
            return false;
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
