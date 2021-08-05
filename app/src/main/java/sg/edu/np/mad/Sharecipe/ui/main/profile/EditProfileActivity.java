package sg.edu.np.mad.Sharecipe.ui.main.profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.common.base.Strings;

import java.io.File;

import java9.util.function.Consumer;
import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.data.UserManager;
import sg.edu.np.mad.Sharecipe.models.User;
import sg.edu.np.mad.Sharecipe.ui.App;
import sg.edu.np.mad.Sharecipe.ui.common.DynamicFocusAppCompatActivity;
import sg.edu.np.mad.Sharecipe.ui.common.OnSingleClickListener;
import sg.edu.np.mad.Sharecipe.ui.common.UiHelper;
import sg.edu.np.mad.Sharecipe.ui.common.textchecks.CheckGroup;
import sg.edu.np.mad.Sharecipe.ui.common.textchecks.InputResult;
import sg.edu.np.mad.Sharecipe.ui.common.textchecks.RequiredFieldCheck;
import sg.edu.np.mad.Sharecipe.ui.common.textchecks.TextLengthChecker;
import sg.edu.np.mad.Sharecipe.utils.DataResult;

public class EditProfileActivity extends DynamicFocusAppCompatActivity {

    private User user;
    private ImageView profilePic;
    private TextInputLayout editUsername;
    private TextInputLayout editBio;

    private String newProfileImagePath;
    private Button updateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        profilePic = findViewById(R.id.editPic);
        editUsername = findViewById(R.id.editUsername);
        editBio = findViewById(R.id.editBio);
        updateButton = findViewById(R.id.saveInfo);

        CheckGroup checkGroup = new CheckGroup()
                .add(editUsername, new RequiredFieldCheck(), new TextLengthChecker(3, 32))
                .add(editBio, new TextLengthChecker(0, 256));

        UserManager userManager = App.getUserManager();
        userManager.getAccountUser().onSuccess(user -> {
            this.user = user;

            runOnUiThread(() -> {
                editUsername.getEditText().setText(user.getUsername());
                editBio.getEditText().setText(user.getBio());
            });

            if (!Strings.isNullOrEmpty(user.getProfileImageId())) {
                userManager.getProfileImage(user)
                        .onSuccess(image -> runOnUiThread(() -> profilePic.setImageBitmap(image)))
                        .onFailedOrError(result -> UiHelper.toastDataResult(EditProfileActivity.this, result));
            }
        });

        profilePic.setOnClickListener((OnSingleClickListener) v -> ImagePicker.with(EditProfileActivity.this)
                .crop()
                .compress(1024)
                .maxResultSize(500, 500)
                .setDismissListener(() -> profilePic.setEnabled(true))
                .start());

        updateButton.setOnClickListener((OnSingleClickListener) v -> {
            InputResult inputResult = checkGroup.parseInputs();
            if (!inputResult.passedAllChecks()) {
                updateButton.setEnabled(true);
                return;
            }

            new MaterialAlertDialogBuilder(EditProfileActivity.this, R.style.AlertDialogCustom)
                    .setTitle("Save Changes")
                    .setMessage("Are you sure you want to save changes?").setCancelable(false)
                    .setCancelable(false)
                    .setPositiveButton("Save", (dialog, which) -> {
                        System.out.println(inputResult.get(editBio));
                        user.setUsername(inputResult.get(editUsername));
                        user.setBio(inputResult.get(editBio));
                        File imageFile = newProfileImagePath == null ? null : new File(newProfileImagePath);

                        Toast.makeText(EditProfileActivity.this, "Saving...", Toast.LENGTH_SHORT).show();

                        userManager.updateAccountUser(user).onSuccess(aVoid -> {
                            if (imageFile == null) {
                                editDone();
                            } else {
                                userManager.setAccountProfileImage(imageFile).onSuccess(aVoid1 -> {
                                    editDone();
                                }).onFailedOrError(editFailed());
                            }
                        }).onFailedOrError(editFailed());
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> {
                        updateButton.setEnabled(true);
                    })
                    .show();
        });
    }

    private <T> Consumer<DataResult<T>> editFailed() {
        return result -> runOnUiThread(() -> {
            updateButton.setEnabled(true);
            UiHelper.toastDataResult(EditProfileActivity.this, result);
        });
    }

    private void editDone() {
        runOnUiThread(() -> {
            Toast.makeText(EditProfileActivity.this, "Saved", Toast.LENGTH_SHORT).show();
            finish();
        });
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