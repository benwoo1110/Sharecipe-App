package sg.edu.np.mad.Sharecipe.ui.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import sg.edu.np.mad.Sharecipe.ui.create.RecipeCreateActivity;
import sg.edu.np.mad.Sharecipe.ui.main.discover.DiscoverFragment;
import sg.edu.np.mad.Sharecipe.ui.main.profile.ProfileFragment;
import sg.edu.np.mad.Sharecipe.ui.main.recipe.MyRecipeFragment;
import sg.edu.np.mad.Sharecipe.ui.main.search.SearchFragment;
import sg.edu.np.mad.Sharecipe.ui.common.FragmentCollection;

import static sg.edu.np.mad.Sharecipe.R.*;

public class MainActivity extends AppCompatActivity {

    private FragmentCollection fragmentCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);

        fragmentCollection = new FragmentCollection();

        BottomNavigationView bottomNavigation = findViewById(id.bottom_navigation);
        FloatingActionButton addRecipe = findViewById(id.button_create_recipe);

        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == id.discover_menu) {
                return viewFragment(DiscoverFragment.class);
            } else if (itemId == id.my_recipes_menu) {
                return viewFragment(MyRecipeFragment.class);
            } else if (itemId == id.search_menu) {
                return viewFragment(SearchFragment.class);
            } else if (itemId == id.profile_menu) {
                return viewFragment(ProfileFragment.class);
            }
            return false;
        });

        bottomNavigation.setSelectedItemId(id.discover_menu);

        addRecipe.setOnClickListener(v -> {
            Intent recipeCreate1 = new Intent(MainActivity.this, RecipeCreateActivity.class);
            startActivity(recipeCreate1);
        });
    }

    private boolean viewFragment(Class<? extends Fragment> fragmentClass) {
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(id.fragment, fragmentCollection.getOrLoad(fragmentClass))
                .commit();
        return true;
    }
}