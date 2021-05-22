package sg.edu.np.mad.Sharecipe;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import java9.util.concurrent.CompletableFuture;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import sg.edu.np.mad.Sharecipe.web.SharecipeRequests;

public class LoginActivity extends AppCompatActivity {

    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText username = findViewById(R.id.loginUsername);
        EditText password = findViewById(R.id.loginPassword);
        Button login = findViewById(R.id.buttonLogin);
        Button signUpNow = findViewById(R.id.buttonSignupNow);

        login.setOnClickListener(v -> SharecipeRequests.helloWorld().thenAccept(response -> {
            String data;
            try {
                data = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            LoginActivity.this.runOnUiThread(() -> Toast.makeText(LoginActivity.this, data, Toast.LENGTH_SHORT).show());
        }).exceptionally(throwable -> {
            LoginActivity.this.runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_SHORT).show());
            return null;
        }));

        signUpNow.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
