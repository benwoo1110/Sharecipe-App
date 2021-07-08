package sg.edu.np.mad.Sharecipe.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.io.IOException;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.data.AccountManager;
import sg.edu.np.mad.Sharecipe.ui.main.MainActivity;
import sg.edu.np.mad.Sharecipe.web.SharecipeRequests;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();
    }

    private void init() {
        AccountManager.getInstance(this).getOrRefreshAccount().onSuccess(account -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }).onFailed(reason -> {
            SplashActivity.this.runOnUiThread(() -> Toast.makeText(SplashActivity.this, reason, Toast.LENGTH_SHORT).show());
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }).onError(error -> SplashActivity.this.runOnUiThread(this::showErrorDialog));
    }

    private void showErrorDialog() {
        new AlertDialog.Builder(SplashActivity.this)
                .setTitle("Whoops!")
                .setMessage("Server error. Make sure you are connected to the internet.")
                .setCancelable(false)
                .setPositiveButton("try again", (dialog, which) -> {
                    init();
                })
                .setNegativeButton("exit", (dialog, which) -> {
                    finish();
                })
                .show();
    }
}