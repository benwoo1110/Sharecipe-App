package sg.edu.np.mad.Sharecipe.ui.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.data.AccountManager;
import sg.edu.np.mad.Sharecipe.data.UserManager;
import sg.edu.np.mad.Sharecipe.models.User;

import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.File;

public class EditProfileActivity extends AppCompatActivity {
    int userid;
    ImageView profilePic;
    Button save;
    TextView editUsername;
    TextView editBio;
    TextView editPassword;
    String profileImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        profilePic = findViewById(R.id.editPic);
        save = findViewById(R.id.saveInfo);
        editUsername = findViewById(R.id.editUsername);
        editBio = findViewById(R.id.editDescription);
        editPassword = findViewById(R.id.editPassword);

        userid = AccountManager.getInstance(EditProfileActivity.this).getAccount().getUserId();

        UserManager.getInstance(EditProfileActivity.this).getProfileImage(userid)
                .onSuccess(image ->  profilePic.setImageBitmap(image))
                .onFailed(reason -> Toast.makeText(this, reason.getMessage(), Toast.LENGTH_SHORT).show())
                .onError(Throwable::printStackTrace);

        UserManager.getInstance(EditProfileActivity.this).getAccountUser().onSuccess(user -> {
            editUsername.setText(user.getUsername());
            editBio.setText(user.getBio());
        });

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(EditProfileActivity.this)
                        .crop()    // Crop image(Optional), Check Customization for more option
                        .compress(1024)    // Final image size will be less than 1 MB
                        .maxResultSize(1080, 1080) // Final image resolution will be less than 1080x1080
                        .start();
            }
        });

        File imageFile = profileImagePath == null ? null : new File(profileImagePath);
        /*editPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManager.getInstance(EditProfileActivity.this).getAccountUser()
            }
        });*/

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
                builder.setTitle("Save changes?");
                builder.setMessage("Are you sure you want to save changes?").setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UserManager.getInstance(EditProfileActivity.this).getAccountUser().onSuccess(user -> {
                            user.setUsername(editUsername.getText().toString());
                            user.setBio(editBio.getText().toString());
                            UserManager.getInstance(EditProfileActivity.this).updateAccountUser(user);
                        });
                        UserManager.getInstance(EditProfileActivity.this).setAccountProfileImage(imageFile);
                        Toast.makeText(EditProfileActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            profilePic.setImageURI(uri);
            profileImagePath = uri.getPath();
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

}