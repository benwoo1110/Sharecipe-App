package sg.edu.np.mad.Sharecipe.ui.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.ui.common.BaseActivity;
import sg.edu.np.mad.Sharecipe.ui.common.MenuStateAdapter;
import sg.edu.np.mad.Sharecipe.ui.main.discover.DiscoverFragment;
import sg.edu.np.mad.Sharecipe.ui.main.profile.ProfileFragment;
import sg.edu.np.mad.Sharecipe.ui.main.recipe.RecipeFragment;
import sg.edu.np.mad.Sharecipe.ui.main.search.SearchFragment;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager2 mainViewPager = findViewById(R.id.mainViewPager);
        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);

        MenuStateAdapter fragmentStateAdapter = new MenuStateAdapter(MainActivity.this)
                .addFragmentClass(DiscoverFragment.class, R.id.discover_menu)
                .addFragmentClass(RecipeFragment.class, R.id.my_recipes_menu)
                .addFragmentClass(SearchFragment.class, R.id.search_menu)
                .addFragmentClass(ProfileFragment.class, R.id.profile_menu);

        mainViewPager.setAdapter(fragmentStateAdapter);
        mainViewPager.setUserInputEnabled(false);
        mainViewPager.setOffscreenPageLimit(3);

        bottomNavigation.setOnItemSelectedListener(item -> {
            mainViewPager.setCurrentItem(fragmentStateAdapter.getMenuPosition(item.getItemId()), false);
            return true;
        });

        bottomNavigation.setSelectedItemId(R.id.discover_menu);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}