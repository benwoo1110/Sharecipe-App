package sg.edu.np.mad.Sharecipe.ui.create.infomation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.NumberPicker;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

import org.threeten.bp.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.models.Recipe;
import sg.edu.np.mad.Sharecipe.models.RecipeTag;
import sg.edu.np.mad.Sharecipe.ui.App;
import sg.edu.np.mad.Sharecipe.ui.common.data.DataSaveable;
import sg.edu.np.mad.Sharecipe.ui.common.listeners.AfterTextChangedWatcher;
import sg.edu.np.mad.Sharecipe.ui.common.BitmapOrUri;
import sg.edu.np.mad.Sharecipe.utils.FormatUtils;

public class InformationFragment extends Fragment implements DataSaveable<Recipe> {

    private final Recipe recipe;
    private final List<Uri> newImagesUris;
    private final List<String> deletedImageIds;

    private TextInputEditText prepTime;
    private AppCompatMultiAutoCompleteTextView tags;
    private TagNamesAdapter tagAdapter;
    private TextInputEditText name;
    private TextInputEditText portions;
    private TextInputEditText description;
    private SwitchMaterial infoPublic;
    private RatingBar difficulty;

    private ImagesAdapter adapter;
    private int hour;
    private int minute;

    public InformationFragment(Recipe recipe, List<Uri> newImagesUris, List<String> deletedImageIds) {
        this.recipe = recipe;
        this.newImagesUris = newImagesUris;
        this.deletedImageIds = deletedImageIds;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_information, container, false);

        RecyclerView imagesView = view.findViewById(R.id.recyclerview_images);
        name = view.findViewById(R.id.infoName);
        prepTime = view.findViewById(R.id.infoPrep);
        portions = view.findViewById(R.id.infoPortions);
        description = view.findViewById(R.id.infoDesc);
        infoPublic = view.findViewById(R.id.infoPublic);
        difficulty = view.findViewById(R.id.infoDifficulty);
        tags = view.findViewById(R.id.recipetag_autocomplete);
        ImageView enlargedImage = view.findViewById(R.id.expanded_image);

        // Name
        if (recipe.getName() != null) {
            name.setText(recipe.getName());
        }

        // Total time needed
        if (recipe.getTotalTimeNeeded() != null) {
            hour = (int) recipe.getTotalTimeNeeded().toHours();
            minute = recipe.getTotalTimeNeeded().toSecondsPart();
            prepTime.setText(FormatUtils.parseDurationShort(recipe.getTotalTimeNeeded()));
        }

        prepTime.setOnTouchListener((v, event) -> {
            prepTime.setEnabled(false);
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                addDurationDialog();
            }
            return false;
        });

        // Portions
        if (recipe.getPortion() > 0) {
            portions.setText(String.valueOf(recipe.getPortion()));
        }

        // View state
        infoPublic.setChecked(recipe.isPublic());

        // Difficulty
        if (recipe.getDifficulty() > 0) {
            difficulty.setRating(recipe.getDifficulty());
        }

        // Description
        if (recipe.getDescription() != null) {
            description.setText(recipe.getDescription());
        }

        // Tags
        if (recipe.getTags() != null) {
            tags.getText().clear();
            for (RecipeTag tag : recipe.getTags()) {
                tags.getText().append(tag.getName()).append(", ");
                createChip(tag.getName());
            }
        }

        MultiAutoCompleteTextView.Tokenizer tokenizer = new MultiAutoCompleteTextView.CommaTokenizer();
        tags.setTokenizer(tokenizer);
        tags.setThreshold(1);

        tags.setOnItemClickListener((parent, view1, position, id) -> {
            createChip(tagAdapter.getItem(position));
        });

        tags.addTextChangedListener((AfterTextChangedWatcher) s -> {
            String[] tagNames = s.toString().split(", ");
            List<RecipeTag> selectedTags = new ArrayList<>();
            for (String tagName : tagNames) {
                selectedTags.add(new RecipeTag(tagName));
            }
            recipe.setTags(selectedTags);
        });

        App.getRecipeManager().getTagSuggestions().onSuccess(tagsNames -> {
            getActivity().runOnUiThread(() -> {
                tagAdapter = new TagNamesAdapter(
                        getContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        tags,
                        tagsNames
                );
                tags.setAdapter(tagAdapter);
            });
        });

        // Images
        adapter = new ImagesAdapter(getActivity(), newImagesUris, deletedImageIds, enlargedImage, view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        imagesView.setAdapter(adapter);
        imagesView.setLayoutManager(gridLayoutManager);

        App.getRecipeManager().getImages(recipe).onSuccess(bitmapMap -> {
            for (String imageId : bitmapMap.keySet()) {
                adapter.addImage(new BitmapOrUri(imageId, bitmapMap.get(imageId)));
            }
            getActivity().runOnUiThread(() -> {
                adapter.notifyDataSetChanged();
            });
        });

        return view;
    }

    public void addDurationDialog() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_duration_picker, null);
        NumberPicker inputHours = view.findViewById(R.id.inputHours);
        NumberPicker inputMinutes = view.findViewById(R.id.inputMinutes);

        // Set valid range of duration
        inputHours.setMaxValue(99);
        inputMinutes.setMaxValue(59);
        inputHours.setMinValue(0);
        inputMinutes.setMinValue(0);

        // Here the hours and minutes are set to the number picker
        inputHours.setValue(hour);
        inputMinutes.setValue(minute);

        new MaterialAlertDialogBuilder(getActivity(), R.style.AlertDialogCustom)
                .setView(view)
                .setTitle("Preparation time")
                .setCancelable(false)
                .setPositiveButton("Confirm", (dialog, which) -> {
                    hour = inputHours.getValue();
                    minute = inputMinutes.getValue();
                    prepTime.setText(String.format(Locale.ENGLISH, "%02d:%02d", hour, minute));
                })
                .setNegativeButton("Cancel", (dialog, which) -> prepTime.setEnabled(true))
                .show();
    }

    private void createChip(String tagName) {
        ChipDrawable chip = ChipDrawable.createFromResource(getActivity(), R.xml.chip);
        chip.setText(tagName);
        chip.setBounds(5, 0, chip.getIntrinsicWidth() + 5, chip.getIntrinsicHeight());
        ImageSpan span = new ImageSpan(chip);
        int endPoint = tags.getSelectionStart();
        tags.getText().setSpan(span, endPoint - tagName.length() - 2, endPoint, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != ImagePicker.REQUEST_CODE) {
            return;
        }

        if (resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            adapter.addImage(new BitmapOrUri(uri));
            adapter.notifyDataSetChanged();
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(getActivity(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDataSaving(Recipe recipe) {
        // Name
        recipe.setName(name.getText().toString());

        // Portions
        recipe.setPortion(FormatUtils.convertToInt(portions.getText().toString()).orElse(0));

        // Time needed
        recipe.setTotalTimeNeeded(Duration.ofHours(hour).plusMinutes(minute));

        // View state
        recipe.setPublic(infoPublic.isChecked());

        // Difficulty
        int recipeDifficulty = Math.round(difficulty.getRating());
        recipe.setDifficulty(recipeDifficulty);

        // Description
        recipe.setDescription(description.getText().toString());

        //Tags
        String[] tagNames = tags.getText().toString().split(", ");
        List<RecipeTag> selectedTags = new ArrayList<>();
        for (String tagName : tagNames) {
            selectedTags.add(new RecipeTag(tagName));
        }
        recipe.setTags(selectedTags);
    }
}
