package sg.edu.np.mad.Sharecipe.ui.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.ui.create.RecipeCreateAdapter;

public class RecipeViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_view);

        TabLayout tabLayout = findViewById(R.id.viewRecipeTab);
        ViewPager2 viewpager = findViewById(R.id.view_recipe_viewpager);

        tabLayout.addTab(tabLayout.newTab().setText("Information"));
        tabLayout.addTab(tabLayout.newTab().setText("Ingredients"));
        tabLayout.addTab(tabLayout.newTab().setText("Steps"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        RecipeViewAdapter adapter = new RecipeViewAdapter(this, tabLayout.getTabCount());
        viewpager.setAdapter(adapter);

        viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
}