package sg.edu.np.mad.Sharecipe.ui.create.steps;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.models.RecipeStep;

public class StepsCreationActivity extends AppCompatActivity {

    private int hours;
    private int minutes;
    private int seconds;

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

        Intent data = getIntent();
        RecipeStep newStep = (RecipeStep)data.getSerializableExtra("New step");
        int stepNumber = data.getIntExtra("Step number", 0);
        String stepDesc = data.getStringExtra("Edit description");
        int timeNeeded = data.getIntExtra("Edit time", 0);

        if (timeNeeded <= 59) {
            input_seconds.setValue(timeNeeded);
        } else if (timeNeeded <= 3599) {
            int minutes = (timeNeeded - timeNeeded % 60) / 60;
            int seconds = timeNeeded - (minutes * 60);

            input_minutes.setValue(minutes);
            input_seconds.setValue(seconds);
        } else {
            int hours = (timeNeeded - timeNeeded % 3600) / 3600;
            int x = (timeNeeded / 60) - (hours * 60);
            int minutes = x - (((x * 60) % 60) / 60);
            int seconds = (timeNeeded - (hours * 3600) - (minutes * 60));

            input_hours.setValue(hours);
            input_minutes.setValue(minutes);
            input_seconds.setValue(seconds);
        }

        input.setText(stepDesc);
        displayStepNo.setText("Step " + stepNumber);

        newStep.setStepNumber(stepNumber);

        input_hours.setOnValueChangedListener((picker, oldVal, newVal) -> {
            hours = input_hours.getValue();
            newStep.setTimeNeeded(convertHours(hours) + convertMinutes(minutes) + seconds);
        });

        input_minutes.setOnValueChangedListener((picker, oldVal, newVal) -> {
            minutes = input_minutes.getValue();
            newStep.setTimeNeeded(convertHours(hours) + convertMinutes(minutes) + seconds);
        });

        input_seconds.setOnValueChangedListener((picker, oldVal, newVal) -> {
            seconds = input_seconds.getValue();
            newStep.setTimeNeeded(convertHours(hours) + convertMinutes(minutes) + seconds);
        });

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                newStep.setDescription(s.toString());
            }
        });

        close.setOnClickListener(v -> {
            if (newStep.getDescription() == null) {
                finish();
            } else {
                checkSaveDialog(newStep);
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

    private void checkSaveDialog(RecipeStep step) {
        new AlertDialog.Builder(this)
                .setTitle("Unsaved changes")
                .setMessage("Would you like to save your changes?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    saveInput(step);
                })
                .setNegativeButton("No", (dialog, which) -> {
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

    private int convertHours(int hours) {
        int seconds = hours * 60 * 60;
        return seconds;
    }

    private int convertMinutes(int minutes) {
        int seconds = minutes * 60;
        return seconds;
    }
}