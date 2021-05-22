package sg.edu.np.mad.Sharecipe;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import sg.edu.np.mad.Sharecipe.Data.UserManager;
import sg.edu.np.mad.Sharecipe.utils.ActionResult;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText username = findViewById(R.id.registerUsername);
        EditText password = findViewById(R.id.registerPassword);
        Button signUp = findViewById(R.id.buttonSignup);

        signUp.setOnClickListener(v -> UserManager.getInstance()
                .register(username.getText().toString(), password.getText().toString())
                .thenAccept(result -> {
                    RegisterActivity.this.runOnUiThread(() -> {
                        Toast toast = new Toast(RegisterActivity.this);
                        toast.setText(result.getMessage());
                        toast.setDuration(Toast.LENGTH_SHORT);
                        toast.show();
                    });
                    if (result instanceof ActionResult.Success) {
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }));
    }
}
