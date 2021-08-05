package sg.edu.np.mad.Sharecipe.ui.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentOnAttachListener;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;

import java.io.Serializable;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.contants.IntentKeys;
import sg.edu.np.mad.Sharecipe.contants.RequestCode;
import sg.edu.np.mad.Sharecipe.models.Recipe;
import sg.edu.np.mad.Sharecipe.ui.App;
import sg.edu.np.mad.Sharecipe.ui.common.BaseActivity;
import sg.edu.np.mad.Sharecipe.ui.common.DataActivity;
import sg.edu.np.mad.Sharecipe.ui.common.listeners.OnTabSelectedListener;
import sg.edu.np.mad.Sharecipe.ui.common.UiHelper;
import sg.edu.np.mad.Sharecipe.ui.create.RecipeCreateActivity;
import sg.edu.np.mad.Sharecipe.ui.view.reviews.RecipeReviewActivity;

public class RecipeViewActivity extends DataActivity<Recipe> {

    private boolean ignoreSelect = false;
    private boolean isLiked;
    private Recipe recipe;
    private BottomNavigationView bottomNavigation;
    private RecipeViewAdapter adapter;
    private int selectedRecipeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_view);

        // Get target recipe to load
        Intent getRecipe = getIntent();
        selectedRecipeId = getRecipe.getIntExtra(IntentKeys.RECIPE_VIEW, -1);
        if (selectedRecipeId == -1) {
            Toast.makeText(RecipeViewActivity.this, "Invalid recipe.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        TabLayout tabLayout = findViewById(R.id.viewRecipeTab);
        ViewPager2 viewpager = findViewById(R.id.view_recipe_viewpager);
        bottomNavigation = findViewById(R.id.viewRecipe_navigation);
        bottomNavigation.setSelectedItemId(R.id.invisibleView);

        // Setup top tabs
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

        // Setup bottom bar menu
        bottomNavigation.setEnabled(false);
        bottomNavigation.getMenu().findItem(R.id.recipe_edit_menu).setVisible(false);
        bottomNavigation.getMenu().findItem(R.id.recipe_like_menu).setVisible(false);
        bottomNavigation.getMenu().findItem(R.id.recipe_delete_menu).setVisible(false);

        bottomNavigation.setOnItemSelectedListener(item -> {
            if (ignoreSelect) {
                ignoreSelect = false;
                return true;
            }

            int itemId = item.getItemId();
            if (itemId == R.id.recipe_review_menu) {
                Intent review = new Intent(RecipeViewActivity.this, RecipeReviewActivity.class);
                review.putExtra(IntentKeys.RECIPE_REVIEW, recipe);
                startActivity(review);
            } else if (itemId == R.id.recipe_edit_menu) {
                Intent editRecipe = new Intent(RecipeViewActivity.this, RecipeCreateActivity.class);
                editRecipe.putExtra(IntentKeys.RECIPE_EDIT, recipe);
                editRecipe.putExtra(IntentKeys.CHECK_RECIPE_EDIT, true);
                startActivityForResult(editRecipe, RequestCode.RECIPE);
            } else if (itemId == R.id.recipe_delete_menu) {
                confirmDeleteDialog();
            } else if (itemId == R.id.recipe_like_menu) {
                item.setEnabled(false);
                if (isLiked) {
                    App.getRecipeManager().accountUnlikeRecipe(recipe).onSuccess(aVoid -> {
                        isLiked = false;
                        runOnUiThread(() -> {
                            updateLikeItem(item);
                            Toast.makeText(RecipeViewActivity.this, "Unliked", Toast.LENGTH_SHORT).show();
                        });
                    }).onFailedOrError(result -> UiHelper.toastDataResult(RecipeViewActivity.this, result));
                } else {
                    App.getRecipeManager().accountLikeRecipe(recipe).onSuccess(aVoid -> {
                        isLiked = true;
                        runOnUiThread(() -> {
                            updateLikeItem(item);
                            Toast.makeText(RecipeViewActivity.this, "Liked", Toast.LENGTH_SHORT).show();
                        });
                    }).onFailedOrError(result -> UiHelper.toastDataResult(RecipeViewActivity.this, result));
                }
            }
            return false;
        });

        // Load the recipe data
        int userID = App.getAccountManager().getAccount().getUserId();
        App.getRecipeManager().get(selectedRecipeId).onSuccess(recipe -> {
            RecipeViewActivity.this.recipe = recipe;

            RecipeViewActivity.this.runOnUiThread(() -> {
                bottomNavigation.setEnabled(true);
                adapter = new RecipeViewAdapter(this, tabLayout.getTabCount(), recipe);
                viewpager.setAdapter(adapter);
            });

            App.getRecipeManager().checkIfAccountLikes(recipe).onSuccess(state -> {
                isLiked = state.getState();
                RecipeViewActivity.this.runOnUiThread(() -> {
                    MenuItem likeItem = bottomNavigation.getMenu().findItem(R.id.recipe_like_menu);
                    updateLikeItem(likeItem);
                    likeItem.setVisible(true);
                    ignoreSelect = true;
                    bottomNavigation.setSelectedItemId(likeItem.getItemId());
                });
            }).thenAccept(ignore -> {
                RecipeViewActivity.this.runOnUiThread(() -> {
                    if (recipe.getUserId() == userID) {
                        bottomNavigation.getMenu().findItem(R.id.recipe_edit_menu).setVisible(true);
                        bottomNavigation.getMenu().findItem(R.id.recipe_delete_menu).setVisible(true);
                    }
                });
            });

        }).onFailedOrError(failed -> {
            runOnUiThread(() -> {
                Toast.makeText(RecipeViewActivity.this, "Failed to load recipe.", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != RequestCode.RECIPE || resultCode != Activity.RESULT_OK || data == null) {
            return;
        }

        Recipe recipe = (Recipe) data.getSerializableExtra(IntentKeys.RECIPE_SAVE);
        this.recipe = recipe;
        callDataLoaded();
    }

    @Override
    protected Recipe supplyData() {
        return recipe;
    }

    private void updateLikeItem(MenuItem likeItem) {
        if (isLiked) {
            likeItem.setIcon(R.drawable.ic_baseline_favorite_24);
            likeItem.setTitle("Unlike");
        } else {
            likeItem.setIcon(R.drawable.outline_favorite_border_24);
            likeItem.setTitle("Like");
        }
        likeItem.setEnabled(true);
    }

    private void confirmDeleteDialog() {
        new MaterialAlertDialogBuilder(this, R.style.AlertDialogCustom)
                .setTitle("Delete recipe")
                .setMessage("Are you sure you want to delete '" + recipe.getName() + "'? This action cannot be reverted!")
                .setPositiveButton("Delete", (dialog, which) -> {
                    App.getRecipeManager().accountDeleteRecipe(recipe).onSuccess(aVoid -> {
                        runOnUiThread(this::finish);
                    });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
