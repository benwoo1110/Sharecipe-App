package sg.edu.np.mad.Sharecipe.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import sg.edu.np.mad.Sharecipe.R;

public class RecipeInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_recipe_info);

        TabLayout tabLayout = findViewById(R.id.recipeTab);
        ViewPager viewPager = findViewById(R.id.recipe_info_viewpager);

        tabLayout.addTab(tabLayout.newTab().setText("Information"));
        tabLayout.addTab(tabLayout.newTab().setText("Steps"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final Adapter_RecipeInfo adapter = new Adapter_RecipeInfo(this, getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

//        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//
  //          override fun onTabSelected(tab: TabLayout.Tab?) {
    //            // Handle tab select
      //      }
//
  //          override fun onTabReselected(tab: TabLayout.Tab?) {
    //            // Handle tab reselect
      //      }
//
  //          override fun onTabUnselected(tab: TabLayout.Tab?) {
    //            // Handle tab unselect
      //      }
        //});
    }
}