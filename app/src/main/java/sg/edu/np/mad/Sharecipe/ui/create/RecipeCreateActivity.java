package sg.edu.np.mad.Sharecipe.ui.create;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import java9.util.concurrent.CompletableFuture;
import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.contants.IntentKeys;
import sg.edu.np.mad.Sharecipe.models.ImageGroup;
import sg.edu.np.mad.Sharecipe.models.ImageRef;
import sg.edu.np.mad.Sharecipe.models.PartialRecipe;
import sg.edu.np.mad.Sharecipe.models.Recipe;
import sg.edu.np.mad.Sharecipe.ui.App;
import sg.edu.np.mad.Sharecipe.ui.common.DynamicFocusAppCompatActivity;
import sg.edu.np.mad.Sharecipe.ui.common.OnTabSelectedListener;

public class RecipeCreateActivity extends DynamicFocusAppCompatActivity {

    private boolean checkEdit;
    private RecipeCreateAdapter adapter;
    private Recipe recipe;

    @Override
    public void onBackPressed() {
        confirmExit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_create);

        Intent getRecipe = getIntent();
        recipe = (Recipe) getRecipe.getSerializableExtra(IntentKeys.RECIPE_EDIT);
        checkEdit = getRecipe.getBooleanExtra(IntentKeys.CHECK_RECIPE_EDIT, false);

        TabLayout tabLayout = findViewById(R.id.recipeTab);
        ViewPager2 viewPager = findViewById(R.id.recipe_info_viewpager);
        BottomNavigationView bottomNavigation = findViewById(R.id.recipe_navigation);
        bottomNavigation.setSelectedItemId(R.id.invisible);

        tabLayout.addTab(tabLayout.newTab().setText("Information"));
        tabLayout.addTab(tabLayout.newTab().setText("Ingredients"));
        tabLayout.addTab(tabLayout.newTab().setText("Steps"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        adapter = new RecipeCreateAdapter(this, tabLayout.getTabCount(), recipe);
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
            if (itemId == R.id.recipe_save_menu) {
                confirmPublish();
                return false;
            }
            return false;
        });
    }

    public void confirmPublish() {
        new AlertDialog.Builder(this, R.style.AlertDialogCustom)
                .setTitle("Confirm changes")
                .setMessage("Are you sure you want to publish this recipe?")
                .setPositiveButton("Confirm", ((dialog, which) -> publishRecipe()))
                .setNegativeButton("No", null)
                .show();
    }

    private void publishRecipe() {
        List<File> imageFiles = new ArrayList<>();
        for (Uri newImagesUris : adapter.getNewImagesUris()) {
            imageFiles.add(new File(newImagesUris.getPath()));
        }

        Toast.makeText(RecipeCreateActivity.this, "Saving...", Toast.LENGTH_SHORT).show();

        if (checkEdit) {
            App.getRecipeManager().update(recipe).onSuccess(updatedRecipe -> {
                List<ImageRef> imageRefs = new ArrayList<>();
                for (String deletedImageId : adapter.getDeletedImageIds()) {
                    imageRefs.add(new ImageRef(deletedImageId));
                }
                ImageGroup deleteImageGroup = new ImageGroup();
                deleteImageGroup.setImageIds(imageRefs);

                CompletableFuture.allOf(
                        App.getRecipeManager().removeImages(updatedRecipe, deleteImageGroup).onFailed(recipeFailed -> {
                            RecipeCreateActivity.this.runOnUiThread(() -> Toast.makeText(RecipeCreateActivity.this, recipeFailed.getMessage(), Toast.LENGTH_SHORT).show());
                        }).onError(Throwable::printStackTrace),
                        App.getRecipeManager().addImages(updatedRecipe, imageFiles)
                ).thenAccept(aVoid -> {
                    App.getRecipeManager().invalidateRecipe(updatedRecipe);
                    RecipeCreateActivity.this.runOnUiThread(() -> {
                        Toast.makeText(RecipeCreateActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.putExtra(IntentKeys.RECIPE_SAVE, updatedRecipe);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    });
                });
            }).onFailed(recipeFailed -> {
                RecipeCreateActivity.this.runOnUiThread(() -> Toast.makeText(RecipeCreateActivity.this, recipeFailed.getMessage(), Toast.LENGTH_SHORT).show());
            });
        }
        else {
            App.getRecipeManager().create(recipe).onSuccess(createdRecipe -> {
                App.getRecipeManager().addImages(createdRecipe, imageFiles).thenAccept(result -> {
                    App.getRecipeManager().invalidateRecipe(createdRecipe);
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
        }
    }

    public void confirmExit() {
        new AlertDialog.Builder(this, R.style.AlertDialogCustom)
                .setTitle("Exit without saving")
                .setMessage("You have unsaved changes. Would you like to exit without saving?")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", ((dialog, which) -> {
                    finish();
                }))
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
