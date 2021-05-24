package sg.edu.np.mad.Sharecipe.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import sg.edu.np.mad.Sharecipe.Data.AccountManager;
import sg.edu.np.mad.Sharecipe.Data.UserManager;
import sg.edu.np.mad.Sharecipe.Models.Account;
import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.utils.DataResult;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //TODO Check if logged in.
        if (AccountManager.getInstance(this).getAccount() == null) {
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        EditText searchText = findViewById(R.id.editTextSearch);
        ImageButton searchButton = findViewById(R.id.buttonSearch);
        TextView usersText = findViewById(R.id.textViewUsers);
        Button refreshButton = findViewById(R.id.buttonRefresh);

        searchButton.setOnClickListener(v -> UserManager.getInstance(this)
                .searchUsers(searchText.getText().toString())
                .onSuccess(userList -> usersText.setText(userList == null ? "No users found!" : String.valueOf(userList)))
                .onFailed(reason -> HomeActivity.this.runOnUiThread(() -> Toast.makeText(HomeActivity.this, reason, Toast.LENGTH_SHORT).show()))
                .onError(error -> HomeActivity.this.runOnUiThread(() -> Toast.makeText(HomeActivity.this, "Server error ;(", Toast.LENGTH_SHORT).show())));

        refreshButton.setOnClickListener(v -> AccountManager.getInstance(this)
                .refresh()
                .onSuccess(account -> {
                    HomeActivity.this.runOnUiThread(() -> {
                        Toast.makeText(HomeActivity.this, account.getAccessToken(), Toast.LENGTH_SHORT).show();
                    });
                }));
    }
}