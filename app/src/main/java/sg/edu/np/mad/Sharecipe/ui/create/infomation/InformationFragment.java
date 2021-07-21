package sg.edu.np.mad.Sharecipe.ui.create.infomation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.models.Recipe;

// TODO: Set limit for images, remove plus button when limit is reached
// TODO: Saving and storing of values for all input fields along with input validation (required fields)
// TODO: Touch up on the UI

public class InformationFragment extends Fragment {

    private ImagesAdapter adapter;

    private final ArrayList<Uri> imageList = new ArrayList<>();
    private final Recipe recipe;
    private final List<File> imageFileList;

    private int hours;
    private int minutes;
    private int seconds;
    private int hoursInSeconds;
    private int minutesInSeconds;
    private TextInputEditText prep;

    public InformationFragment(Recipe recipe, List<File> imageFileList) {
        this.recipe = recipe;
        this.imageFileList = imageFileList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_information, container, false);

        RecyclerView images = view.findViewById(R.id.recyclerview_images);
        TextInputEditText name = view.findViewById(R.id.infoName);
        prep = view.findViewById(R.id.infoPrep);
        TextInputEditText portions = view.findViewById(R.id.infoPortions);
        SwitchMaterial infoPublic = view.findViewById(R.id.infoPublic);
        RatingBar difficulty = view.findViewById(R.id.infoDifficulty);
        ImageView enlargedImage = view.findViewById(R.id.expanded_image);

        adapter = new ImagesAdapter(getActivity(), imageList, imageFileList, enlargedImage, view);
        LinearLayoutManager cLayoutManager = new LinearLayoutManager(getActivity());
        cLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        images.setAdapter(adapter);
        images.setLayoutManager(cLayoutManager);

        recipe.setName("test");
        recipe.setPortion(0);
        recipe.setDifficulty(0);
        recipe.setTotalTimeNeeded(120);

        prep.setText("00:00");

        prep.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                addDurationDialog(prep.getText().toString());
                return false;
            }
        });

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                recipe.setName(s.toString());
            }
        });

        difficulty.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            int recipeDifficulty = difficulty.getNumStars();
            recipe.setDifficulty(recipeDifficulty);
        });

        int recipePortions = 0;
        try {
            recipePortions = Integer.parseInt(portions.getText().toString());
        }
        catch (NumberFormatException ignored) {
        }
        recipe.setPortion(recipePortions);

        return view;
    }

    public void addDurationDialog(String currentDuration) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.duration_picker, null);
        NumberPicker inputHours = view.findViewById(R.id.inputHours);
        NumberPicker inputMinutes = view.findViewById(R.id.inputMinutes);

        // Current time input is passed in and set as hours and minutes respectively
        String[] durationSplit = currentDuration.split(":");
        hours = Integer.parseInt(durationSplit[0]);
        minutes = Integer.parseInt(durationSplit[1]);

        inputHours.setMaxValue(99);
        inputMinutes.setMaxValue(59);
        inputHours.setMinValue(0);
        inputMinutes.setMinValue(0);

        // Here the hours and minutes are set to the number picker
        inputHours.setValue(hours);
        inputMinutes.setValue(minutes);


        // Pickers have a listener, when the value is changed the hour/minute is set to the new value and its time in seconds is saved
        inputHours.setOnValueChangedListener((picker, oldVal, newVal) -> {
            hours = inputHours.getValue();
            hoursInSeconds = hours * 3600;
        });

        inputMinutes.setOnValueChangedListener((picker, oldVal, newVal) -> {
            minutes = inputMinutes.getValue();
            minutesInSeconds = minutes * 60;
        });

        new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Preparation time")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // On confirm the total time in seconds is added and this is set to recipe total time needed, then the hour and
                        seconds = hoursInSeconds + minutesInSeconds;
                        recipe.setTotalTimeNeeded(seconds);
                        if (hours < 10) {
                            if (minutes < 10) {
                                prep.setText("0" + String.valueOf(hours) + ":" + "0" + String.valueOf(minutes));
                            }
                            else {
                                prep.setText("0" + String.valueOf(hours) + ":" + String.valueOf(minutes));
                            }
                        }
                        else if (minutes < 10) {
                            prep.setText(String.valueOf(hours) + ":" + "0" + String.valueOf(minutes));
                        }
                        else {
                            prep.setText(String.valueOf(hours) + ":" + String.valueOf(minutes));
                        }

                    }
                })
                .setNegativeButton("Cancel", null)
                .show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != ImagePicker.REQUEST_CODE) {
            return;
        }

        if (resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            imageList.add(uri);
            imageFileList.add(new File(uri.getPath()));
            adapter.notifyDataSetChanged();
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(getActivity(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }
}
