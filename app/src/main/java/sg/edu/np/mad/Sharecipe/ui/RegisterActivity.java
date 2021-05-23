package sg.edu.np.mad.Sharecipe.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import sg.edu.np.mad.Sharecipe.Data.UserManager;
import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.utils.ActionResult;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_register);

        TextInputLayout username = findViewById(R.id.registerUsername);
        TextInputLayout password = findViewById(R.id.registerPassword);
        Button signUp = findViewById(R.id.buttonSignup);

        signUp.setOnClickListener(v -> UserManager.getInstance()
                .register(username.getEditText().getText().toString(), password.getEditText().getText().toString())
                .thenAccept(result -> {
                    RegisterActivity.this.runOnUiThread(() -> Toast.makeText(RegisterActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show());
                    if (result instanceof ActionResult.Success) {
                        Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }));
    }
}
