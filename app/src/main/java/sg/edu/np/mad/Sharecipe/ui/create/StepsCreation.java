package sg.edu.np.mad.Sharecipe.ui.create;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.models.RecipeStep;

public class StepsCreation extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps_creation);

        TextView displayStepNo = findViewById(R.id.display_StepNum);
        // TODO: Add option to input amount of time needed for this step
        ImageButton close = findViewById(R.id.buttonCloseStep);
        ImageButton save = findViewById(R.id.buttonSaveStep);
        EditText input = findViewById(R.id.input_StepDesc);

        Intent intent = getIntent();
        RecipeStep newStep = (RecipeStep)intent.getSerializableExtra("New step");
        int stepNumber = intent.getIntExtra("Step number", 0);

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                newStep.setDescription(input.getText().toString());
                newStep.setStepNumber(stepNumber);
                // TODO: Set time needed;

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        displayStepNo.setText("Step " + stepNumber);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (input.getText() == null) {
                    // TODO: Exit without doing anything, close activity?
                }

                else {
                    checkSaveDialog(newStep);
                }
            }
        });



    }

    private void checkSaveDialog(RecipeStep step) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Unsaved changes");
        builder.setMessage("Would you like to save your changes?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            // TODO: Call method to save changes
            saveInput(step);
        });
        builder.setNegativeButton("No", (dialog, which) -> {
            // TODO: Remove changes and pass back nothing
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void saveInput(RecipeStep returnStep) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("Input step", returnStep);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}