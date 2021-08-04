package sg.edu.np.mad.Sharecipe.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.app.ActivityOptionsCompat;

import com.google.android.material.textfield.TextInputLayout;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.ui.common.DynamicFocusAppCompatActivity;
import sg.edu.np.mad.Sharecipe.ui.common.OnSingleClickListener;
import sg.edu.np.mad.Sharecipe.ui.common.UiHelper;
import sg.edu.np.mad.Sharecipe.ui.common.textchecks.CheckGroup;
import sg.edu.np.mad.Sharecipe.ui.common.textchecks.InputResult;
import sg.edu.np.mad.Sharecipe.ui.common.textchecks.RequiredFieldCheck;
import sg.edu.np.mad.Sharecipe.ui.main.MainActivity;

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

        CheckGroup checkGroup = new CheckGroup()
                .add(username, new RequiredFieldCheck())
                .add(password, new RequiredFieldCheck());

        login.setOnClickListener((OnSingleClickListener) v -> {
            hideSoftKeyBoard();

            InputResult inputResult = checkGroup.parseInputs();
            if (!inputResult.passedAllChecks()) {
                login.setEnabled(true);
                return;
            }

            App.getAccountManager().login(inputResult.get(username), inputResult.get(password)).onSuccess(result -> {
                Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(
                        LoginActivity.this,
                        android.R.anim.fade_in,
                        android.R.anim.fade_out
                ).toBundle();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent, bundle);
            }).onFailedOrError(result -> UiHelper.uiThread(() -> {
                login.setEnabled(true);
                UiHelper.toastDataResult(LoginActivity.this, result);
            }));
        });

        signUpNow.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
