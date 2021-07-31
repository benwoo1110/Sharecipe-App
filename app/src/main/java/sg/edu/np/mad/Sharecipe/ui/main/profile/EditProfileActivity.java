package sg.edu.np.mad.Sharecipe.ui.main.profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.github.dhaval2404.imagepicker.listener.DismissListener;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;

import java9.util.concurrent.CompletableFuture;
import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.data.AccountManager;
import sg.edu.np.mad.Sharecipe.data.UserManager;
import sg.edu.np.mad.Sharecipe.models.User;
import sg.edu.np.mad.Sharecipe.ui.App;
import sg.edu.np.mad.Sharecipe.ui.LoginActivity;
import sg.edu.np.mad.Sharecipe.ui.common.DynamicFocusAppCompatActivity;
import sg.edu.np.mad.Sharecipe.ui.common.OnSingleClickListener;

public class EditProfileActivity extends DynamicFocusAppCompatActivity {

    private User user;
    private ImageView profilePic;
    private TextInputLayout editUsername;
    private TextInputLayout editBio;

    private String newProfileImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        profilePic = findViewById(R.id.editPic);
        editUsername = findViewById(R.id.editUsername);
        editBio = findViewById(R.id.editBio);
        Button updateButton = findViewById(R.id.saveInfo);

        UserManager userManager = App.getUserManager();
        userManager.getAccountUser().onSuccess(user -> {
            this.user = user;

            runOnUiThread(() -> {
                editUsername.getEditText().setText(user.getUsername());
                editBio.getEditText().setText(user.getBio());
            });

            userManager.getProfileImage(user)
                    .onSuccess(image -> runOnUiThread(() -> profilePic.setImageBitmap(image)))
                    .onFailed(reason -> Toast.makeText(this, reason.getMessage(), Toast.LENGTH_SHORT).show())
                    .onError(Throwable::printStackTrace);
        });

        profilePic.setOnClickListener((OnSingleClickListener) v -> ImagePicker.with(EditProfileActivity.this)
                .crop()
                .compress(1024)
                .maxResultSize(500, 500)
                .setDismissListener(() -> profilePic.setEnabled(true))
                .start());

        updateButton.setOnClickListener((OnSingleClickListener) v -> new AlertDialog.Builder(EditProfileActivity.this)
                .setTitle("Save Changes")
                .setMessage("Are you sure you want to save changes?").setCancelable(false)
                .setCancelable(false)
                .setPositiveButton("Save", (dialog, which) -> {
                    File imageFile = newProfileImagePath == null ? null : new File(newProfileImagePath);
                    user.setUsername(editUsername.getEditText().getText().toString());
                    user.setBio(editBio.getEditText().getText().toString());
                    Toast.makeText(EditProfileActivity.this, "Saving...", Toast.LENGTH_SHORT).show();
                    userManager.invalidateUser(user);
                    CompletableFuture.allOf(
                            userManager.updateAccountUser(user),
                            userManager.setAccountProfileImage(imageFile)
                    ).thenAccept(aVoid -> {
                        runOnUiThread(() -> {
                            Toast.makeText(EditProfileActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                            finish();
                        });
                    });
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    updateButton.setEnabled(true);
                })
                .show());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != ImagePicker.REQUEST_CODE) {
            return;
        }

        profilePic.setEnabled(true);

        if (resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            profilePic.setImageURI(uri);
            newProfileImagePath = uri.getPath();
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }
}