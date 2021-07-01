package sg.edu.np.mad.Sharecipe.ui;

import android.content.Intent;
import android.graphics.Color;
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
        setContentView(R.layout.activity_register);

        TextInputLayout username = findViewById(R.id.registerUsername);
        TextInputLayout bio = findViewById(R.id.registerBio);
        TextInputLayout password = findViewById(R.id.registerPassword);
        TextInputLayout passwordConfirm = findViewById(R.id.registerConfirmPassword);
        Button signUp = findViewById(R.id.buttonSignup);

        signUp.setOnClickListener(v -> {
            hideSoftKeyBoard();

            String usernameText = username.getEditText().getText().toString();
            String bioText = bio.getEditText().getText().toString();
            String passwordText = password.getEditText().getText().toString();
            String passwordConfirmText = passwordConfirm.getEditText().getText().toString();

            if (!passwordText.equals(passwordConfirmText)) {
                passwordConfirm.setError("Password does not match.");
                return;
            }

            AccountManager.getInstance(this)
                    .register(usernameText, passwordText, bioText)
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
