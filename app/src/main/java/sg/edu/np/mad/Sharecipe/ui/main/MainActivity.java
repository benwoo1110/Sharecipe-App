package sg.edu.np.mad.Sharecipe.ui.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import sg.edu.np.mad.Sharecipe.data.AccountManager;
import sg.edu.np.mad.Sharecipe.ui.LoginActivity;
import sg.edu.np.mad.Sharecipe.ui.create.RecipeCreateActivity;
import sg.edu.np.mad.Sharecipe.ui.common.FragmentCollection;

import static sg.edu.np.mad.Sharecipe.R.*;

public class MainActivity extends AppCompatActivity {

    private FragmentCollection fragmentCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);

        fragmentCollection = new FragmentCollection();

        //TODO Check if logged in.
        if (AccountManager.getInstance(this).getAccount() == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return;
        }

        FloatingActionButton recipeCreate = findViewById(id.button_create_recipe);
        BottomNavigationView bottomNavigation = findViewById(id.bottom_navigation);

        bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == id.discover_menu) {
                return viewFragment(DiscoverFragment.class);
            } else if (itemId == id.my_recipes_menu) {
                return viewFragment(MyRecipeFragment.class);
            } else if (itemId == id.search_menu) {
                return viewFragment(UserSearchFragment.class);
            } else if (itemId == id.profile_menu) {
                return viewFragment(ProfileFragment.class);
            }
            return false;
        });

        bottomNavigation.setSelectedItemId(id.discover_menu);

        recipeCreate.setOnClickListener(v -> {
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

//        EditText searchText = findViewById(R.id.editTextSearch);
//        ImageButton searchButton = findViewById(R.id.buttonSearch);
//        TextView usersText = findViewById(R.id.textViewUsers);
//        Button recipeButton = findViewById(R.id.buttonRecipe);
//
//        searchButton.setOnClickListener(v -> {
//            usersText.setText("Loading...");
//            UserManager.getInstance(this)
//                    .search(searchText.getText().toString())
//                    .onSuccess(userList -> usersText.setText(userList == null ? "No users found!" : String.valueOf(userList)))
//                    .onFailed(usersText::setText)
//                    .onError(error -> MainActivity.this.runOnUiThread(() -> Toast.makeText(MainActivity.this, "Server error ;(", Toast.LENGTH_SHORT).show()));
//        });
//
//        recipeButton.setOnClickListener(v -> {
//            Recipe newRecipe = new Recipe();
//            newRecipe.setName("Testing");
//            newRecipe.setDifficulty(10);
//            List<RecipeStep> steps = new ArrayList<RecipeStep>() {{
//                add(new RecipeStep(1, "bah", "boop"));
//                add(new RecipeStep(2, "lah", "mee"));
//            }};
//            newRecipe.setSteps(steps);
//
//            RecipeManager.getInstance(this).save(newRecipe)
//                    .onSuccess(recipe -> usersText.setText(String.valueOf(recipe)))
//                    .onFailed(reason -> this.runOnUiThread(() -> Toast.makeText(MainActivity.this, reason, Toast.LENGTH_SHORT).show()))
//                    .onError(error -> this.runOnUiThread(() -> Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show()));
//        });
//
//        refreshButton.setOnClickListener(v -> AccountManager.getInstance(this)
//                .refresh()
//                .onSuccess(account -> {
//                    MainActivity.this.runOnUiThread(() -> {
//                        Toast.makeText(MainActivity.this, account.getAccessToken(), Toast.LENGTH_SHORT).show();
//                    });
//                }));
}