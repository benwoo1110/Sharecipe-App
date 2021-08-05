package sg.edu.np.mad.Sharecipe.ui.create.steps;

import androidx.appcompat.app.AlertDialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.contants.IntentKeys;
import sg.edu.np.mad.Sharecipe.models.RecipeStep;
import sg.edu.np.mad.Sharecipe.ui.common.AfterTextChangedWatcher;
import sg.edu.np.mad.Sharecipe.ui.common.DynamicFocusAppCompatActivity;
import sg.edu.np.mad.Sharecipe.ui.common.OnSingleClickListener;
import sg.edu.np.mad.Sharecipe.ui.common.textchecks.CheckGroup;
import sg.edu.np.mad.Sharecipe.ui.common.textchecks.RequiredFieldCheck;
import sg.edu.np.mad.Sharecipe.ui.common.textchecks.TextLengthChecker;

public class StepsCreationActivity extends DynamicFocusAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps_creation);

        Intent data = getIntent();
        RecipeStep step = (RecipeStep) data.getSerializableExtra(IntentKeys.RECIPE_STEP_EDIT);

        TextView displayStepNo = findViewById(R.id.display_StepNum);
        MaterialButton close = findViewById(R.id.buttonCloseStep);
        MaterialButton save = findViewById(R.id.buttonSaveStep);
        TextInputLayout input = findViewById(R.id.input_StepDesc);

        CheckGroup checkGroup = new CheckGroup()
                .add(input, new RequiredFieldCheck(), new TextLengthChecker(8, 1024));

        displayStepNo.setText("Step " + step.getStepNumber());
        input.getEditText().setText(step.getDescription());

        String originalDesc = step.getDescription();

        input.getEditText().addTextChangedListener((AfterTextChangedWatcher) s -> step.setDescription(s.toString()));

        close.setOnClickListener(v -> {
            if (input.getEditText().getText().toString().equals(originalDesc)) {
                finish();
            } else if (input.getEditText().getText().toString().equals("")) {
                finish();
            } else {
                checkSaveDialog(step, originalDesc);
            }
        });

        save.setOnClickListener((OnSingleClickListener) v -> {
            if (!checkGroup.checkAll()) {
                save.setEnabled(true);
                return;
            }
            saveInput(step);
        });
    }

    private void checkSaveDialog(RecipeStep step, String stepDesc) {
        new MaterialAlertDialogBuilder(StepsCreationActivity.this, R.style.AlertDialogCustom)
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
