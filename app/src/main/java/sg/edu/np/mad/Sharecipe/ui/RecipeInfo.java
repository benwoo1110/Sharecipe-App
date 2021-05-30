package sg.edu.np.mad.Sharecipe.ui;

import androidx.appcompat.app.AppCompatActivity;
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
        TabItem tabInfo = findViewById(R.id.tabInfo);

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