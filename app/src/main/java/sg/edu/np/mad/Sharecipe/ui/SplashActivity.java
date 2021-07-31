package sg.edu.np.mad.Sharecipe.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.ui.main.MainActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();
    }

    private void init() {
        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(
                SplashActivity.this,
                android.R.anim.fade_in,
                android.R.anim.fade_out
        ).toBundle();

        App.getAccountManager().getOrRefreshAccount().onSuccess(account -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent, bundle);
        }).onFailed(reason -> {
            SplashActivity.this.runOnUiThread(() -> Toast.makeText(SplashActivity.this, reason.getMessage(), Toast.LENGTH_SHORT).show());
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent, bundle);
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
