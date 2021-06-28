package sg.edu.np.mad.Sharecipe.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import sg.edu.np.mad.Sharecipe.data.AccountManager;
import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.ui.common.DynamicFocusAppCompatActivity;
import sg.edu.np.mad.Sharecipe.ui.main.MainActivity;

public class RegisterActivity extends DynamicFocusAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_register);

        TextInputLayout username = findViewById(R.id.registerUsername);
        TextInputLayout password = findViewById(R.id.registerPassword);
        Button signUp = findViewById(R.id.buttonSignup);

        signUp.setOnClickListener(v -> {
            hideSoftKeyBoard();
            AccountManager.getInstance(this)
                    .register(username.getEditText().getText().toString(), password.getEditText().getText().toString())
                    .onSuccess(account -> {
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    })
                    .onFailed(reason -> {
                        RegisterActivity.this.runOnUiThread(() -> Toast.makeText(RegisterActivity.this, reason, Toast.LENGTH_SHORT).show());
                    })
                    .onError(error -> {
                        RegisterActivity.this.runOnUiThread(() -> Toast.makeText(RegisterActivity.this, "Server error ;(", Toast.LENGTH_SHORT).show());
                    });
        });
    }
}
