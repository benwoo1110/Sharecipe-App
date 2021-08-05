package sg.edu.np.mad.Sharecipe.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.ui.common.BaseActivity;
import sg.edu.np.mad.Sharecipe.ui.common.listeners.OnSingleClickListener;
import sg.edu.np.mad.Sharecipe.ui.common.UiHelper;
import sg.edu.np.mad.Sharecipe.ui.common.textchecks.CheckGroup;
import sg.edu.np.mad.Sharecipe.ui.common.textchecks.ConfirmMatchCheck;
import sg.edu.np.mad.Sharecipe.ui.common.textchecks.InputResult;
import sg.edu.np.mad.Sharecipe.ui.common.textchecks.RequiredFieldCheck;
import sg.edu.np.mad.Sharecipe.ui.common.textchecks.TextLengthChecker;
import sg.edu.np.mad.Sharecipe.ui.main.MainActivity;

public class RegisterActivity extends BaseActivity {

    private ImageView profileImage;
    private String profileImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextInputLayout username = findViewById(R.id.registerUsername);
        TextInputLayout bio = findViewById(R.id.registerBio);
        TextInputLayout password = findViewById(R.id.registerPassword);
        TextInputLayout passwordConfirm = findViewById(R.id.registerConfirmPassword);
        Button signUp = findViewById(R.id.buttonSignup);

        CheckGroup checkGroup = new CheckGroup()
                .add(username, new RequiredFieldCheck(), new TextLengthChecker(3, 32))
                .add(bio, new TextLengthChecker(0, 256))
                .add(password, new RequiredFieldCheck(), new TextLengthChecker(8, 64))
                .add(passwordConfirm, new RequiredFieldCheck(), new ConfirmMatchCheck(password));

        profileImage = findViewById(R.id.registerProfileImage);
        profileImage.setOnClickListener((OnSingleClickListener) v -> {
            ImagePicker.with(this)
                    .crop()	// Crop image(Optional), Check Customization for more option
                    .compress(1024)	// Final image size will be less than 1 MB
                    .maxResultSize(500, 500) // Final image resolution will be less than 500x500
                    .setDismissListener(() -> profileImage.setEnabled(true))
                    .start();
        });

        signUp.setOnClickListener((OnSingleClickListener) v -> {
            hideSoftKeyBoard();

            InputResult inputResult = checkGroup.parseInputs();
            if (!inputResult.passedAllChecks()) {
                signUp.setEnabled(true);
                return;
            }

            File imageFile = profileImagePath == null ? null : new File(profileImagePath);

            App.getAccountManager().register(inputResult.get(username), inputResult.get(password), inputResult.get(bio)).onSuccess(account -> {
                if (imageFile == null) {
                    goToHome();
                } else {
                    App.getUserManager().setAccountProfileImage(imageFile)
                            .onFailedOrError(result -> UiHelper.toastDataResult(RegisterActivity.this, result))
                            .thenAccept(result -> goToHome());
                }
            }).onFailedOrError(result -> UiHelper.uiThread(() -> {
                UiHelper.toastDataResult(RegisterActivity.this, result);
                signUp.setEnabled(true);
            }));
        });
    }

    private void goToHome() {
        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(
                RegisterActivity.this,
                android.R.anim.fade_in,
                android.R.anim.fade_out
        ).toBundle();
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent, bundle);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != ImagePicker.REQUEST_CODE) {
            return;
        }

        profileImage.setEnabled(true);

        if (resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            profileImage.setImageURI(uri);
            profileImagePath = uri.getPath();
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }
}
