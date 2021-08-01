package sg.edu.np.mad.Sharecipe.ui.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.contants.IntentKeys;
import sg.edu.np.mad.Sharecipe.models.Recipe;
import sg.edu.np.mad.Sharecipe.models.RecipeReview;
import sg.edu.np.mad.Sharecipe.models.RecipeStep;
import sg.edu.np.mad.Sharecipe.ui.App;
import sg.edu.np.mad.Sharecipe.ui.common.OnTabSelectedListener;
import sg.edu.np.mad.Sharecipe.ui.create.RecipeCreateActivity;
import sg.edu.np.mad.Sharecipe.ui.view.reviews.RecipeReviewActivity;
import sg.edu.np.mad.Sharecipe.ui.view.reviews.RecipeReviewAdapter;

public class RecipeViewActivity extends AppCompatActivity {
    public static int LAUNCH_REVIEW_CREATION = 1;

    private boolean ignoreSelect = false;
    private boolean isLiked;
    private Recipe recipe;
    private BottomNavigationView bottomNavigation;
    private RecipeViewAdapter adapter;
    private List<RecipeReview> reviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_view);

        // Get target recipe to load
        Intent getRecipe = getIntent();
        int selectedRecipeId = getRecipe.getIntExtra(IntentKeys.RECIPE_VIEW, -1);
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
                App.getRecipeManager().get(selectedRecipeId).onSuccess(recipe -> {
                    Intent review = new Intent(RecipeViewActivity.this, RecipeReviewActivity.class);
                    review.putExtra(IntentKeys.RECIPE_REVIEW, recipe);
                    startActivityForResult(review, LAUNCH_REVIEW_CREATION);
                }).onFailed(System.out::println).onError(Throwable::printStackTrace);
            } else if (itemId == R.id.recipe_edit_menu) {
                Intent editRecipe = new Intent(RecipeViewActivity.this, RecipeCreateActivity.class);
                editRecipe.putExtra(IntentKeys.RECIPE_EDIT, recipe);
                editRecipe.putExtra(IntentKeys.CHECK_RECIPE_EDIT, true);
                startActivity(editRecipe);
            } else if (itemId == R.id.recipe_delete_menu) {
                confirmDeleteDialog(recipe);
            } else if (itemId == R.id.recipe_like_menu) {
                item.setEnabled(false);
                if (isLiked) {
                    App.getRecipeManager().accountUnlikeRecipe(recipe).onSuccess(aVoid -> {
                        isLiked = false;
                        runOnUiThread(() -> updateLikeItem(item));
                    }).onFailed(System.out::println).onError(Throwable::printStackTrace);
                } else {
                    App.getRecipeManager().accountLikeRecipe(recipe).onSuccess(aVoid -> {
                        isLiked = true;
                        runOnUiThread(() -> updateLikeItem(item));
                    }).onFailed(System.out::println).onError(Throwable::printStackTrace);
                }
            }
            return false;
        });

        // Load the recipe data
        int userID = App.getAccountManager().getAccount().getUserId();
        App.getRecipeManager().get(selectedRecipeId).onSuccess(recipe -> {
            RecipeViewActivity.this.recipe = recipe;

            RecipeViewActivity.this.runOnUiThread(() -> {
                adapter = new RecipeViewAdapter(this, tabLayout.getTabCount(), recipe);
                viewpager.setAdapter(adapter);
                if (recipe.getUserId() == userID) {
                    bottomNavigation.getMenu().findItem(R.id.recipe_edit_menu).setVisible(true);
                    bottomNavigation.getMenu().findItem(R.id.recipe_delete_menu).setVisible(true);
                }
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
            });

        }).onFailed(failed -> {
            runOnUiThread(() -> {
                Toast.makeText(RecipeViewActivity.this, "Failed to load recipe.", Toast.LENGTH_SHORT).show();
                finish();
            });
        }).onError(Throwable::printStackTrace);
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

    private void confirmDeleteDialog(Recipe recipe) {
        new AlertDialog.Builder(this, R.style.AlertDialogCustom)
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != LAUNCH_REVIEW_CREATION) {
            return;
        }

        if (resultCode == Activity.RESULT_OK) {
            RecipeReview newReview = (RecipeReview) data.getSerializableExtra(IntentKeys.RECIPE_REVIEW_SAVE);
            if (recipe.getReviews() == null) {
                reviews.add(newReview);
                recipe.setReviews(reviews);
            }
            else {
                recipe.getReviews().add(newReview);
            }
            adapter.notifyDataSetChanged();
        }
    }
}
