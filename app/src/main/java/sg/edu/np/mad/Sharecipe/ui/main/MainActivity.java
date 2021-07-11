package sg.edu.np.mad.Sharecipe.ui.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.ui.common.MenuStateAdapter;
import sg.edu.np.mad.Sharecipe.ui.create.RecipeCreateActivity;
import sg.edu.np.mad.Sharecipe.ui.main.discover.DiscoverFragment;
import sg.edu.np.mad.Sharecipe.ui.main.profile.ProfileFragment;
import sg.edu.np.mad.Sharecipe.ui.main.recipe.MyRecipeFragment;
import sg.edu.np.mad.Sharecipe.ui.main.search.SearchFragment;
import sg.edu.np.mad.Sharecipe.ui.common.FragmentCollection;

public class MainActivity extends AppCompatActivity {

    private FragmentCollection fragmentCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentCollection = new FragmentCollection();

        ViewPager2 mainViewPager = findViewById(R.id.mainViewPager);
        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);

        MenuStateAdapter fragmentStateAdapter = new MenuStateAdapter(MainActivity.this)
                .addFragmentClass(DiscoverFragment.class, R.id.discover_menu)
                .addFragmentClass(MyRecipeFragment.class, R.id.my_recipes_menu)
                .addFragmentClass(SearchFragment.class, R.id.search_menu)
                .addFragmentClass(ProfileFragment.class, R.id.profile_menu);

        mainViewPager.setAdapter(fragmentStateAdapter);
        mainViewPager.setUserInputEnabled(false);

        bottomNavigation.setOnItemSelectedListener(item -> {
            mainViewPager.setCurrentItem(fragmentStateAdapter.getMenuPosition(item.getItemId()), false);
            return true;
        });

        bottomNavigation.setSelectedItemId(R.id.discover_menu);


    }
}