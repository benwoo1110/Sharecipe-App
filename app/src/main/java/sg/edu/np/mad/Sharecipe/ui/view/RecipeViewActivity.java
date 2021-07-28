package sg.edu.np.mad.Sharecipe.ui.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.contants.IntentKeys;
import sg.edu.np.mad.Sharecipe.models.PartialRecipe;
import sg.edu.np.mad.Sharecipe.models.Recipe;
import sg.edu.np.mad.Sharecipe.ui.App;
import sg.edu.np.mad.Sharecipe.ui.common.OnTabSelectedListener;
import sg.edu.np.mad.Sharecipe.ui.create.RecipeCreateAdapter;

public class RecipeViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_view);

        Intent getRecipe = getIntent();
        PartialRecipe selectedRecipe = (PartialRecipe) getRecipe.getSerializableExtra(IntentKeys.RECIPE_VIEW_INTENT);

        TabLayout tabLayout = findViewById(R.id.viewRecipeTab);
        ViewPager2 viewpager = findViewById(R.id.view_recipe_viewpager);

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

        App.getRecipeManager().get(selectedRecipe.getRecipeId()).onSuccess(recipe -> {
            RecipeViewActivity.this.runOnUiThread(() -> {
                RecipeViewAdapter adapter = new RecipeViewAdapter(this, tabLayout.getTabCount(), recipe);
                viewpager.setAdapter(adapter);
            });
        }).onFailed(failed -> {
            Toast.makeText(RecipeViewActivity.this, "Failed to load recipe.", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
