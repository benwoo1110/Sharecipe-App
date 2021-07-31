package sg.edu.np.mad.Sharecipe.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.ui.common.DynamicFocusAppCompatActivity;
import sg.edu.np.mad.Sharecipe.ui.common.OnSingleClickListener;
import sg.edu.np.mad.Sharecipe.ui.common.textchecks.CheckGroup;
import sg.edu.np.mad.Sharecipe.ui.common.textchecks.ConfirmMatchCheck;
import sg.edu.np.mad.Sharecipe.ui.common.textchecks.RequiredFieldCheck;
import sg.edu.np.mad.Sharecipe.ui.main.MainActivity;

public class RegisterActivity extends DynamicFocusAppCompatActivity {

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

        profileImage = findViewById(R.id.registerProfileImage);
        profileImage.setOnClickListener(v -> {
            ImagePicker.with(this)
                    .crop()	// Crop image(Optional), Check Customization for more option
                    .compress(1024)	// Final image size will be less than 1 MB
                    .maxResultSize(500, 500) // Final image resolution will be less than 500x500
                    .start();
        });

        CheckGroup checkGroup = new CheckGroup()
                .add(username, new RequiredFieldCheck())
                .add(password, new RequiredFieldCheck())
                .add(passwordConfirm, new RequiredFieldCheck(), new ConfirmMatchCheck(password));

        signUp.setOnClickListener((OnSingleClickListener) v -> {
            hideSoftKeyBoard();

            if (!checkGroup.checkAll()) {
                signUp.setEnabled(true);
                return;
            }

            String usernameText = username.getEditText().getText().toString();
            String bioText = bio.getEditText().getText().toString();
            String passwordText = password.getEditText().getText().toString();

            File imageFile = profileImagePath == null ? null : new File(profileImagePath);

            App.getAccountManager().register(usernameText, passwordText, bioText).onSuccess(account -> {
                App.getUserManager().setAccountProfileImage(imageFile).onFailed(reason -> {
                    RegisterActivity.this.runOnUiThread(() -> Toast.makeText(RegisterActivity.this, reason.getMessage(), Toast.LENGTH_SHORT).show());
                }).thenAccept(result -> {
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                });
            }).onFailed(reason -> {
                RegisterActivity.this.runOnUiThread(() -> Toast.makeText(RegisterActivity.this, reason.getMessage(), Toast.LENGTH_SHORT).show());
            }).onError(error -> {
                RegisterActivity.this.runOnUiThread(() -> Toast.makeText(RegisterActivity.this, "Server error ;(", Toast.LENGTH_SHORT).show());
            });
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != ImagePicker.REQUEST_CODE) {
            return;
        }

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
