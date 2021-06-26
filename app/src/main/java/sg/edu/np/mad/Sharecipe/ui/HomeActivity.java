package sg.edu.np.mad.Sharecipe.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import sg.edu.np.mad.Sharecipe.data.AccountManager;
import sg.edu.np.mad.Sharecipe.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragment, new LoadingFragment())
                    .commit();
        }

        //TODO Check if logged in.
        if (AccountManager.getInstance(this).getAccount() == null) {
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return;
        }

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment, new UserSearchFragment())
                .commit();

        FloatingActionButton recipeCreate = findViewById(R.id.buttonCreate);

        recipeCreate.setOnClickListener(v -> {
            Intent recipeCreate1 = new Intent(HomeActivity.this, RecipeCreateActivity.class);
            startActivity(recipeCreate1);
        });

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
//                    .onError(error -> HomeActivity.this.runOnUiThread(() -> Toast.makeText(HomeActivity.this, "Server error ;(", Toast.LENGTH_SHORT).show()));
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
//                    .onFailed(reason -> this.runOnUiThread(() -> Toast.makeText(HomeActivity.this, reason, Toast.LENGTH_SHORT).show()))
//                    .onError(error -> this.runOnUiThread(() -> Toast.makeText(HomeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show()));
//        });
//
//        refreshButton.setOnClickListener(v -> AccountManager.getInstance(this)
//                .refresh()
//                .onSuccess(account -> {
//                    HomeActivity.this.runOnUiThread(() -> {
//                        Toast.makeText(HomeActivity.this, account.getAccessToken(), Toast.LENGTH_SHORT).show();
//                    });
//                }));
    }
}