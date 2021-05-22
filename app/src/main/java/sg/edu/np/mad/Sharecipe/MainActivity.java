package sg.edu.np.mad.Sharecipe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import sg.edu.np.mad.Sharecipe.Data.UserManager;
import sg.edu.np.mad.Sharecipe.Models.User;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView welcome = findViewById(R.id.textViewWelcome);
        welcome.setText(UserManager.getInstance().getAccount().toString());
    }
}