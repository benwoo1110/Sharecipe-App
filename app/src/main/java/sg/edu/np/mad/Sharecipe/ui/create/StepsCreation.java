package sg.edu.np.mad.Sharecipe.ui.create;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.models.RecipeStep;

public class StepsCreation extends AppCompatActivity {

    int hours;
    int minutes;
    int seconds;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps_creation);

        TextView displayStepNo = findViewById(R.id.display_StepNum);
        ImageButton close = findViewById(R.id.buttonCloseStep);
        ImageButton save = findViewById(R.id.buttonSaveStep);
        TextInputEditText input = findViewById(R.id.input_StepDesc);
        NumberPicker input_hours = findViewById(R.id.inputHours);
        NumberPicker input_minutes = findViewById(R.id.inputMinutes);
        NumberPicker input_seconds = findViewById(R.id.inputSeconds);

        input_hours.setMaxValue(99);
        input_hours.setMinValue(0);
        input_minutes.setMaxValue(59);
        input_minutes.setMinValue(0);
        input_seconds.setMaxValue(59);
        input_seconds.setMinValue(0);

        Intent intent = getIntent();
        RecipeStep newStep = (RecipeStep)intent.getSerializableExtra("New step");
        int stepNumber = intent.getIntExtra("Step number", 0);
        displayStepNo.setText("Step " + stepNumber);


        input_hours.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                hours = input_hours.getValue();
                newStep.setTimeNeeded(convertHours(hours) + convertMinutes(minutes) + seconds);
            }
        });

        input_minutes.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                minutes = input_minutes.getValue();
                newStep.setTimeNeeded(convertHours(hours) + convertMinutes(minutes) + seconds);
            }
        });

        input_seconds.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                seconds = input_seconds.getValue();
                newStep.setTimeNeeded(convertHours(hours) + convertMinutes(minutes) + seconds);
            }
        });

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                newStep.setDescription(input.getText().toString());
                newStep.setStepNumber(stepNumber);

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });



        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newStep.getDescription() == null) {
                    finish();
                }

                else {
                    checkSaveDialog(newStep);
                }
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newStep.getDescription() != null) {
                    saveInput(newStep);
                }
                else {
                    Toast.makeText(StepsCreation.this, "You have not typed in anything", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void checkSaveDialog(RecipeStep step) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Unsaved changes");
        builder.setMessage("Would you like to save your changes?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            saveInput(step);
        });
        builder.setNegativeButton("No", (dialog, which) -> {
            finish();
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

    private int convertHours(int hours) {
        int seconds = hours * 60 * 60;
        return seconds;
    }

    private int convertMinutes(int minutes) {
        int seconds = minutes * 60;
        return seconds;
    }

}