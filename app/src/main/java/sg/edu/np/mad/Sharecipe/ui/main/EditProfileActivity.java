package sg.edu.np.mad.Sharecipe.ui.main;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

import sg.edu.np.mad.Sharecipe.R;

public class EditProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        TextView editUsername = findViewById(R.id.editUsername);
        TextView editBio = findViewById(R.id.editDescription);
        TextView editPassword = findViewById(R.id.editPassword);

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}