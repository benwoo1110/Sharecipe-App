package sg.edu.np.mad.Sharecipe.ui.main;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

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

public class EditProfileActivity extends AppCompatActivity {
    int userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        ImageView profilePic = findViewById(R.id.editPic);
        Button save = findViewById(R.id.saveInfo);
        TextView editUsername = findViewById(R.id.editUsername);
        TextView editBio = findViewById(R.id.editDescription);
        TextView editPassword = findViewById(R.id.editPassword);

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

        //UserManager.getInstance(EditProfileActivity.this).setAccountProfileImage();
        /*editPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManager.getInstance(EditProfileActivity.this).getAccountUser()
            }
        });*/

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManager.getInstance(EditProfileActivity.this).getAccountUser().onSuccess(user -> {
                    user.setUsername(editUsername.getText().toString());
                    user.setBio(editBio.getText().toString());
                    UserManager.getInstance(EditProfileActivity.this).updateAccountUser(user);
                });
                Toast.makeText(EditProfileActivity.this, "Saved", Toast.LENGTH_SHORT).show();
            }
        });
    }

}