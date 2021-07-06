package sg.edu.np.mad.Sharecipe.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;

import sg.edu.np.mad.Sharecipe.data.AccountManager;
import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.ui.common.DynamicFocusAppCompatActivity;
import sg.edu.np.mad.Sharecipe.ui.main.MainActivity;
import sg.edu.np.mad.Sharecipe.web.SharecipeRequests;

public class LoginActivity extends DynamicFocusAppCompatActivity {

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextInputLayout username = findViewById(R.id.loginUsername);
        TextInputLayout password = findViewById(R.id.loginPassword);
        Button login = findViewById(R.id.buttonLogin);
        Button signUpNow = findViewById(R.id.buttonSignupNow);

        login.setOnClickListener(v -> {
            hideSoftKeyBoard();
            AccountManager.getInstance(this)
                    .login(username.getEditText().getText().toString(), password.getEditText().getText().toString())
                    .onSuccess(result -> {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    })
                    .onFailed(reason -> LoginActivity.this.runOnUiThread(() -> Toast.makeText(LoginActivity.this, reason.getMessage(), Toast.LENGTH_SHORT).show()))
                    .onError(error -> {
                        LoginActivity.this.runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Server error ;(", Toast.LENGTH_SHORT).show());
                    });
        });

        signUpNow.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
