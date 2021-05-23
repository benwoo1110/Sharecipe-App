package sg.edu.np.mad.Sharecipe.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import sg.edu.np.mad.Sharecipe.Data.UserManager;
import sg.edu.np.mad.Sharecipe.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //TODO Check if logged in.
        if (UserManager.getInstance().getAccount() == null) {
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        EditText searchText = findViewById(R.id.editTextSearch);
        ImageButton searchButton = findViewById(R.id.buttonSearch);
        TextView usersText = findViewById(R.id.textViewUsers);

        searchButton.setOnClickListener(v -> UserManager.getInstance()
                .searchUsers(searchText.getText().toString())
                .thenAccept(userList -> usersText.setText(userList == null ? "No users found!" : String.valueOf(userList))));
    }
}