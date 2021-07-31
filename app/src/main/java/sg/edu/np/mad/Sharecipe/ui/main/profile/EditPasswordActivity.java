package sg.edu.np.mad.Sharecipe.ui.main.profile;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.data.UserManager;
import sg.edu.np.mad.Sharecipe.ui.App;
import sg.edu.np.mad.Sharecipe.ui.common.DynamicFocusAppCompatActivity;
import sg.edu.np.mad.Sharecipe.ui.common.OnSingleClickListener;
import sg.edu.np.mad.Sharecipe.ui.common.textchecks.CheckGroup;
import sg.edu.np.mad.Sharecipe.ui.common.textchecks.ConfirmMatchCheck;
import sg.edu.np.mad.Sharecipe.ui.common.textchecks.RequiredFieldCheck;
import sg.edu.np.mad.Sharecipe.ui.common.textchecks.TextLengthChecker;

public class EditPasswordActivity extends DynamicFocusAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);

        ImageView profileImage = findViewById(R.id.profileImage);
        TextInputLayout oldPassword = findViewById(R.id.editOldPassword);
        TextInputLayout newPassword = findViewById(R.id.editNewPassword);
        TextInputLayout confirmPassword = findViewById(R.id.editConfirmPassword);
        Button updateButton = findViewById(R.id.saveButton);

        CheckGroup checkGroup = new CheckGroup()
                .add(oldPassword, new RequiredFieldCheck())
                .add(newPassword, new RequiredFieldCheck(), new TextLengthChecker(8, 64))
                .add(confirmPassword, new RequiredFieldCheck(), new ConfirmMatchCheck(newPassword));

        UserManager userManager = App.getUserManager();
        userManager.getAccountUser().onSuccess(user -> {
            userManager.getProfileImage(user)
                    .onSuccess(image -> runOnUiThread(() -> profileImage.setImageBitmap(image)))
                    .onFailed(reason -> Toast.makeText(this, reason.getMessage(), Toast.LENGTH_SHORT).show())
                    .onError(Throwable::printStackTrace);
        });

        updateButton.setOnClickListener((OnSingleClickListener) v -> {
            if (!checkGroup.checkAll()) {
                updateButton.setEnabled(true);
                return;
            }

            new AlertDialog.Builder(EditPasswordActivity.this)
                    .setTitle("Save Changes")
                    .setMessage("Are you sure you want to save changes?").setCancelable(false)
                    .setCancelable(false)
                    .setPositiveButton("Save", (dialog, which) -> {
                        String oldPasswordText = oldPassword.getEditText().getText().toString();
                        String newPasswordText = newPassword.getEditText().getText().toString();

                        Toast.makeText(EditPasswordActivity.this, "Saving", Toast.LENGTH_SHORT).show();
                        App.getAccountManager().changePassword(oldPasswordText, newPasswordText).onSuccess(aVoid -> {
                            runOnUiThread(() -> {
                                Toast.makeText(EditPasswordActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                                finish();
                            });
                        });
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> {
                        updateButton.setEnabled(true);
                    })
                    .show();
        });
    }
}
