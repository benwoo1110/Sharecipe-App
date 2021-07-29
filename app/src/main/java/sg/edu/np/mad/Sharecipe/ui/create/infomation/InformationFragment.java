package sg.edu.np.mad.Sharecipe.ui.create.infomation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import sg.edu.np.mad.Sharecipe.ui.common.AfterTextChangedWatcher;
import sg.edu.np.mad.Sharecipe.utils.FormatUtils;

// TODO: Set limit for images, remove plus button when limit is reached
// TODO: Saving and storing of values for all input fields along with input validation (required fields)
// TODO: Touch up on the UI

public class InformationFragment extends Fragment {

    private ImagesAdapter adapter;

    private final ArrayList<Uri> imageList = new ArrayList<>();
    private final Recipe recipe;
    private final List<File> imageFileList;
    private final List<RecipeTag> recipeTags = new ArrayList<>();

    private TextInputEditText prep;
    private MultiAutoCompleteTextView tags;

    public InformationFragment(Recipe recipe, List<File> imageFileList) {
        this.recipe = recipe;
        this.imageFileList = imageFileList;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_information, container, false);

        RecyclerView images = view.findViewById(R.id.recyclerview_images);
        TextInputEditText name = view.findViewById(R.id.infoName);
        prep = view.findViewById(R.id.infoPrep);
        TextInputEditText portions = view.findViewById(R.id.infoPortions);
        TextInputEditText description = view.findViewById(R.id.infoDesc);
        SwitchMaterial infoPublic = view.findViewById(R.id.infoPublic);
        RatingBar difficulty = view.findViewById(R.id.infoDifficulty);
        tags = view.findViewById(R.id.recipetag_autocomplete);
        ImageView enlargedImage = view.findViewById(R.id.expanded_image);

        createTags();
        TagNamesAdapter tagAdapter = new TagNamesAdapter(
                getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                tags,
                TagNamesAdapter.convertToTagNames(recipeTags)
        );

        tags.setAdapter(tagAdapter);
        tags.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        tags.setThreshold(1);

        tags.setOnItemClickListener((parent, view1, position, id) -> {
            createRecipientChip(tagAdapter.getItem(position));
        });

        tags.addTextChangedListener((AfterTextChangedWatcher) s -> {
            //TODO This is a bad way to do this.
            String[] tagNames = s.toString().split(", ");
            List<RecipeTag> selectedTags = new ArrayList<>();
            for (String tagName : tagNames) {
                for (RecipeTag recipeTag : recipeTags) {
                    if (tagName.equals(recipeTag.getName())) {
                        selectedTags.add(recipeTag);
                        break;
                    }
                }
            }
            recipe.setTags(selectedTags);
        });

        adapter = new ImagesAdapter(getActivity(), imageList, imageFileList, enlargedImage, view);
        LinearLayoutManager cLayoutManager = new LinearLayoutManager(getActivity());
        cLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        images.setAdapter(adapter);
        images.setLayoutManager(cLayoutManager);

        prep.setText(FormatUtils.parseDurationShort(recipe.getTotalTimeNeeded()));
        prep.setOnTouchListener((v, event) -> {
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
            int recipeDifficulty = difficulty.getNumStars();
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

        new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Preparation time")
                .setPositiveButton("Confirm", (dialog, which) -> {
                    // On confirm the total time in seconds is added and this is set to recipe total time needed, then the hour and
                    Duration newTotalTimeNeeded = Duration.ofHours(inputHours.getValue()).plusMinutes(inputMinutes.getValue());
                    recipe.setTotalTimeNeeded(newTotalTimeNeeded);
                    prep.setText(FormatUtils.parseDurationShort(recipe.getTotalTimeNeeded()));
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    public void createTags() {
        RecipeTag chinese = new RecipeTag();
        chinese.setName("Chinese");
        RecipeTag malay = new RecipeTag();
        malay.setName("Malay");
        RecipeTag indian = new RecipeTag();
        indian.setName("Indian");
        RecipeTag american = new RecipeTag();
        american.setName("American");
        RecipeTag japanese = new RecipeTag();
        japanese.setName("Japanese");
        RecipeTag korean = new RecipeTag();
        korean.setName("Korean");
        RecipeTag italian = new RecipeTag();
        italian.setName("Italian");
        RecipeTag vietnamese = new RecipeTag();
        vietnamese.setName("Vietnamese");
        RecipeTag thai = new RecipeTag();
        thai.setName("Thai");

        RecipeTag rice = new RecipeTag();
        rice.setName("Rice");
        RecipeTag noodles = new RecipeTag();
        noodles.setName("Noodles");
        RecipeTag sandwich = new RecipeTag();
        sandwich.setName("Sandwich");
        RecipeTag burger = new RecipeTag();
        burger.setName("Burger");
        RecipeTag meat = new RecipeTag();
        meat.setName("Meat");
        RecipeTag vegetarian = new RecipeTag();
        vegetarian.setName("Vegetarian");
        RecipeTag seafood = new RecipeTag();
        seafood.setName("Seafood");
        RecipeTag snack = new RecipeTag();
        snack.setName("Snack");
        RecipeTag drink = new RecipeTag();
        drink.setName("Drink");
        RecipeTag dessert = new RecipeTag();
        dessert.setName("Dessert");

        recipeTags.add(chinese);
        recipeTags.add(malay);
        recipeTags.add(indian);
        recipeTags.add(american);
        recipeTags.add(japanese);
        recipeTags.add(korean);
        recipeTags.add(italian);
        recipeTags.add(vietnamese);
        recipeTags.add(thai);
        recipeTags.add(rice);
        recipeTags.add(noodles);
        recipeTags.add(sandwich);
        recipeTags.add(burger);
        recipeTags.add(meat);
        recipeTags.add(vegetarian);
        recipeTags.add(seafood);
        recipeTags.add(snack);
        recipeTags.add(drink);
        recipeTags.add(dessert);
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
