package sg.edu.np.mad.Sharecipe.ui.create;

import androidx.appcompat.app.AppCompatActivity;

import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import java9.util.function.Consumer;
import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.data.RecipeManager;
import sg.edu.np.mad.Sharecipe.models.Recipe;
import sg.edu.np.mad.Sharecipe.models.RecipeStep;

public class RecipeCreateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_create);

        TabLayout tabLayout = findViewById(R.id.recipeTab);
        ViewPager2 viewPager = findViewById(R.id.recipe_info_viewpager);

        tabLayout.addTab(tabLayout.newTab().setText("Information"));
        tabLayout.addTab(tabLayout.newTab().setText("Steps"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        RecipeCreateAdapter adapter = new RecipeCreateAdapter(this, tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        Recipe recipe = new Recipe();
        recipe.setName("test");

        List<RecipeStep> recipeSteps = new ArrayList<>();
        recipeSteps.add(new RecipeStep(1, "test", "stuff"));
        recipe.setSteps(recipeSteps);

        RecipeManager.getInstance(RecipeCreateActivity.this).save(recipe).onSuccess(new Consumer<Recipe>() {
            @Override
            public void accept(Recipe recipe) {

            }
        }).onFailed(new Consumer<String>() {
            @Override
            public void accept(String reason) {

            }
        });

    }
}