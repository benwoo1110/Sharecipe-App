package sg.edu.np.mad.Sharecipe.ui.create;

import android.net.Uri;

import  androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import sg.edu.np.mad.Sharecipe.models.Recipe;
import sg.edu.np.mad.Sharecipe.ui.ErrorFragment;
import sg.edu.np.mad.Sharecipe.ui.create.infomation.InformationFragment;
import sg.edu.np.mad.Sharecipe.ui.create.ingredients.IngredientFragment;
import sg.edu.np.mad.Sharecipe.ui.create.steps.StepsFragment;

public class RecipeCreateAdapter extends FragmentStateAdapter {

    private final int totalTabs;
    private final Recipe recipe;
    private final List<Uri> newImagesUris;
    private final List<String> deletedImageIds;

    public RecipeCreateAdapter(FragmentActivity fa, int totalTabs, Recipe recipe) {
        super(fa);
        this.totalTabs = totalTabs;
        this.recipe = recipe;
        this.newImagesUris = new ArrayList<>();
        this.deletedImageIds = new ArrayList<>();
    }

    @NotNull
    @Override
    public Fragment createFragment(int position) {
        switch(position) {
            case 0:
                return new InformationFragment(recipe, newImagesUris, deletedImageIds);
            case 1:
                return new IngredientFragment(recipe);
            case 2:
                return new StepsFragment(recipe);
            default:
                return new ErrorFragment();
        }
    }

    @Override
    public int getItemCount() {
        return totalTabs;
    }

    public List<Uri> getNewImagesUris() {
        return newImagesUris;
    }

    public List<String> getDeletedImageIds() {
        return deletedImageIds;
    }
}
