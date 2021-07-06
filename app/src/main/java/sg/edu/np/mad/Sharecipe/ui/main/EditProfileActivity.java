package sg.edu.np.mad.Sharecipe.ui.main;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.data.UserManager;

public class EditProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        TextView editUsername = findViewById(R.id.editUsername);
        TextView editBio = findViewById(R.id.editDescription);
        TextView editPassword = findViewById(R.id.editPassword);
        Button save = findViewById(R.id.saveInfo);

        UserManager.getInstance(EditProfileActivity.this).getAccountUser().onSuccess(user -> {
            editUsername.setText(user.getUsername());
            editBio.setText(user.getBio());
        });

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
                    user.setUsername(editUsername.toString());
                    user.setBio(editBio.toString());
                });
            }
        });

    }
}