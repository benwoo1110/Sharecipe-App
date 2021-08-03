package sg.edu.np.mad.Sharecipe.ui.create.infomation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import androidx.appcompat.app.AlertDialog;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

import org.threeten.bp.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.models.Recipe;
import sg.edu.np.mad.Sharecipe.models.RecipeTag;
import sg.edu.np.mad.Sharecipe.ui.App;
import sg.edu.np.mad.Sharecipe.ui.common.AfterTextChangedWatcher;
import sg.edu.np.mad.Sharecipe.ui.common.UiHelper;
import sg.edu.np.mad.Sharecipe.ui.create.RecipeCreateActivity;
import sg.edu.np.mad.Sharecipe.utils.FormatUtils;

public class InformationFragment extends Fragment {

    private ImagesAdapter adapter;

    private final Recipe recipe;
    private final List<Uri> newImagesUris;
    private final List<String> deletedImageIds;

    private TextInputEditText prep;
    private AppCompatMultiAutoCompleteTextView tags;
    private TagNamesAdapter tagAdapter;

    public InformationFragment(Recipe recipe, List<Uri> newImagesUris, List<String> deletedImageIds) {
        this.recipe = recipe;
        this.newImagesUris = newImagesUris;
        this.deletedImageIds = deletedImageIds;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_information, container, false);

        RecyclerView images = view.findViewById(R.id.recyclerview_images);
        TextInputEditText name = view.findViewById(R.id.infoName);
        prep = view.findViewById(R.id.infoPrep);
        TextInputEditText portions = view.findViewById(R.id.infoPortions);
        TextInputEditText description = view.findViewById(R.id.infoDesc);
        SwitchMaterial infoPublic = view.findViewById(R.id.infoPublic);
        RatingBar difficulty = view.findViewById(R.id.infoDifficulty);
        tags = view.findViewById(R.id.recipetag_autocomplete);
        ImageView enlargedImage = view.findViewById(R.id.expanded_image);

        if (recipe.getName() != null) {
            name.setText(recipe.getName());
        }

        if (recipe.getDifficulty() > 0) {
            difficulty.setRating(recipe.getDifficulty());
        }

        if (recipe.getPortion() > 0) {
            portions.setText(String.valueOf(recipe.getPortion()));
        }

        infoPublic.setChecked(recipe.isPublic());

        if (recipe.getTotalTimeNeeded() != null) {
            prep.setText(FormatUtils.parseDurationShort(recipe.getTotalTimeNeeded()));
        }

        if (recipe.getTags() != null) {
            tags.getText().clear();
            for (RecipeTag tag : recipe.getTags()) {
                tags.getText().append(tag.getName() + ", ");
                createRecipientChip(tag.getName());
            }
        }

        if (recipe.getDescription() != null) {
            description.setText(recipe.getDescription());
        }

        MultiAutoCompleteTextView.Tokenizer tokenizer = new MultiAutoCompleteTextView.CommaTokenizer();
        tags.setTokenizer(tokenizer);
        tags.setThreshold(1);

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

        tags.setOnItemClickListener((parent, view1, position, id) -> {
            createRecipientChip(tagAdapter.getItem(position));
        });

        tags.addTextChangedListener((AfterTextChangedWatcher) s -> {
            String[] tagNames = s.toString().split(", ");
            List<RecipeTag> selectedTags = new ArrayList<>();
            for (String tagName : tagNames) {
                selectedTags.add(new RecipeTag(tagName));
            }
            recipe.setTags(selectedTags);
        });

        adapter = new ImagesAdapter(getActivity(), newImagesUris, deletedImageIds, enlargedImage, view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);

        images.setAdapter(adapter);
        images.setLayoutManager(gridLayoutManager);

        App.getRecipeManager().getImages(recipe).onSuccess(bitmapMap -> {
            for (String imageId : bitmapMap.keySet()) {
                adapter.addImage(new BitmapOrUri(imageId, bitmapMap.get(imageId)));
            }
            getActivity().runOnUiThread(() -> {
                adapter.notifyDataSetChanged();
            });
        });

        prep.setText(FormatUtils.parseDurationShort(recipe.getTotalTimeNeeded()));
        prep.setOnTouchListener((v, event) -> {
            prep.setEnabled(false);
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                addDurationDialog();
            }
            return false;
        });

        name.addTextChangedListener((AfterTextChangedWatcher) s -> recipe.setName(s.toString()));

        portions.addTextChangedListener((AfterTextChangedWatcher) s -> {
            int portionsNumber = FormatUtils.convertToInt(s.toString()).orElse(0);
            recipe.setPortion(portionsNumber);
        });

        description.addTextChangedListener((AfterTextChangedWatcher) s -> recipe.setDescription(s.toString()));

        difficulty.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            int recipeDifficulty = Math.round(difficulty.getRating());
            recipe.setDifficulty(recipeDifficulty);
        });

        infoPublic.setOnCheckedChangeListener((buttonView, isChecked) -> recipe.setPublic(isChecked));

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
        inputHours.setValue((int) recipe.getTotalTimeNeeded().toHours());
        inputMinutes.setValue(recipe.getTotalTimeNeeded().toMinutesPart());

        new AlertDialog.Builder(getActivity(), R.style.AlertDialogCustom)
                .setView(view)
                .setTitle("Preparation time")
                .setCancelable(false)
                .setPositiveButton("Confirm", (dialog, which) -> {
                    // On confirm the total time in seconds is added and this is set to recipe total time needed, then the hour and
                    Duration newTotalTimeNeeded = Duration.ofHours(inputHours.getValue()).plusMinutes(inputMinutes.getValue());
                    recipe.setTotalTimeNeeded(newTotalTimeNeeded);
                    prep.setText(FormatUtils.parseDurationShort(recipe.getTotalTimeNeeded()));
                    prep.setEnabled(true);
                })
                .setNegativeButton("Cancel", (dialog, which) -> prep.setEnabled(true))
                .show();
    }

    private void createRecipientChip(String tagName) {
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
}
