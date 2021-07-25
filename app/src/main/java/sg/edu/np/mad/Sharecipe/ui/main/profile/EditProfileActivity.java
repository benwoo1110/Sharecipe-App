package sg.edu.np.mad.Sharecipe.ui.main.profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.File;

import java9.util.concurrent.CompletableFuture;
import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.data.UserManager;
import sg.edu.np.mad.Sharecipe.models.User;
import sg.edu.np.mad.Sharecipe.ui.App;
import sg.edu.np.mad.Sharecipe.ui.common.DynamicFocusAppCompatActivity;

public class EditProfileActivity extends DynamicFocusAppCompatActivity {

    private User user;
    private ImageView profilePic;
    private TextView editUsername;
    private TextView editBio;
    private TextView editPassword;
    private Button saveButton;

    private String newProfileImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        profilePic = findViewById(R.id.editPic);
        saveButton = findViewById(R.id.saveInfo);
        editUsername = findViewById(R.id.editUsername);
        editBio = findViewById(R.id.editDescription);
        editPassword = findViewById(R.id.editPassword);

        UserManager userManager = App.getUserManager();

        userManager.getAccountUser().onSuccess(user -> {
            this.user = user;
            editUsername.setText(user.getUsername());
            editBio.setText(user.getBio());

            userManager.getProfileImage(user)
                    .onSuccess(image -> runOnUiThread(() -> profilePic.setImageBitmap(image)))
                    .onFailed(reason -> Toast.makeText(this, reason.getMessage(), Toast.LENGTH_SHORT).show())
                    .onError(Throwable::printStackTrace);
        });

        profilePic.setOnClickListener(v -> ImagePicker.with(EditProfileActivity.this)
                .crop()
                .compress(1024)
                .maxResultSize(500, 500)
                .start());

        saveButton.setOnClickListener(v -> {
            new AlertDialog.Builder(EditProfileActivity.this)
                    .setTitle("Save Changes")
                    .setMessage("Are you sure you want to save changes?").setCancelable(false)
                    .setCancelable(false)
                    .setPositiveButton("Save", (dialog, which) -> {
                        File imageFile = newProfileImagePath == null ? null : new File(newProfileImagePath);
                        user.setUsername(editUsername.getText().toString());
                        user.setBio(editBio.getText().toString());
                        
                        Toast.makeText(EditProfileActivity.this, "Saving...", Toast.LENGTH_SHORT).show();
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
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        /*editPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManager.getInstance(EditProfileActivity.this).getAccountUser()
            }
        });*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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