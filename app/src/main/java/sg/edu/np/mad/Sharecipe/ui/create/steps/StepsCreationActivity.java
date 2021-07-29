package sg.edu.np.mad.Sharecipe.ui.create.steps;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.common.base.Strings;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.contants.IntentKeys;
import sg.edu.np.mad.Sharecipe.models.RecipeStep;
import sg.edu.np.mad.Sharecipe.ui.common.AfterTextChangedWatcher;

public class StepsCreationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps_creation);

        TextView displayStepNo = findViewById(R.id.display_StepNum);
        ImageButton close = findViewById(R.id.buttonCloseStep);
        ImageButton save = findViewById(R.id.buttonSaveStep);
        TextInputEditText input = findViewById(R.id.input_StepDesc);

        Intent data = getIntent();
        RecipeStep step = (RecipeStep) data.getSerializableExtra(IntentKeys.RECIPE_STEP_EDIT);

        displayStepNo.setText("Step " + step.getStepNumber());
        input.setText(step.getDescription());

        String originalDesc = step.getDescription();

        input.addTextChangedListener((AfterTextChangedWatcher) s -> step.setDescription(s.toString()));

        close.setOnClickListener(v -> {
            if (input.getText().toString().equals(originalDesc)) {
                finish();
            } else if (input.getText().toString().equals("")) {
                finish();
            } else {
                checkSaveDialog(step, originalDesc);
            }
        });

        save.setOnClickListener(v -> {
            if (Strings.isNullOrEmpty(step.getDescription())) {
                Toast.makeText(StepsCreationActivity.this, "You have not typed in anything", Toast.LENGTH_SHORT).show();
            } else {
                saveInput(step);
            }
        });
    }

    private void checkSaveDialog(RecipeStep step, String stepDesc) {
        new AlertDialog.Builder(this)
                .setTitle("Unsaved changes")
                .setMessage("Would you like to save your changes?")
                .setPositiveButton("Yes", (dialog, which) -> saveInput(step))
                .setNegativeButton("No", (dialog, which) -> {
                    step.setDescription(stepDesc);
                    finish();
                })
                .show();
    }

    private void saveInput(RecipeStep returnStep) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(IntentKeys.RECIPE_STEP_SAVE, returnStep);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
