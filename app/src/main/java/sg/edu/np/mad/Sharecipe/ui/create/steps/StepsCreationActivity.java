package sg.edu.np.mad.Sharecipe.ui.create.steps;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import sg.edu.np.mad.Sharecipe.R;
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
        RecipeStep newStep = (RecipeStep) data.getSerializableExtra("New step");
        int stepNumber = data.getIntExtra("Step number", 0);
        String stepDesc = data.getStringExtra("Edit description");

        input.setText(stepDesc);
        displayStepNo.setText("Step " + stepNumber);

        newStep.setStepNumber(stepNumber);

        input.addTextChangedListener((AfterTextChangedWatcher) s -> newStep.setDescription(s.toString()));

        close.setOnClickListener(v -> {

            // TODO: BEN SEE THIS WHY IT NO WORK??
            if (input.getText().toString() != stepDesc) {
                checkSaveDialog(newStep, stepDesc);
            } else {
                finish();
            }
        });

        save.setOnClickListener(v -> {
            if (newStep.getDescription() != null) {
                saveInput(newStep);
            } else {
                Toast.makeText(StepsCreationActivity.this, "You have not typed in anything", Toast.LENGTH_SHORT).show();
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
        returnIntent.putExtra("Input step", returnStep);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}