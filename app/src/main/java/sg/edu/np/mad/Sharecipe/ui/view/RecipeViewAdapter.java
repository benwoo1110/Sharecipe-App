package sg.edu.np.mad.Sharecipe.ui.view;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.jetbrains.annotations.NotNull;

import sg.edu.np.mad.Sharecipe.models.Recipe;
import sg.edu.np.mad.Sharecipe.ui.ErrorFragment;
import sg.edu.np.mad.Sharecipe.ui.view.information.ViewInformationFragment;
import sg.edu.np.mad.Sharecipe.ui.view.ingredients.ViewIngredientsFragment;
import sg.edu.np.mad.Sharecipe.ui.view.steps.ViewStepsFragment;

public class RecipeViewAdapter extends FragmentStateAdapter {

    private final int totalTabs;
    private final Recipe recipe;

    public RecipeViewAdapter(FragmentActivity fa, int totalTabs, Recipe recipe) {
        super(fa);
        this.totalTabs = totalTabs;
        this.recipe = recipe;
    }

    @NotNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ViewInformationFragment(recipe);
            case 1:
                return new ViewIngredientsFragment(recipe);
            case 2:
                return new ViewStepsFragment(recipe);
            default:
                return new ErrorFragment();
        }
    }
    @Override
    public int getItemCount() {
        return totalTabs;
    }
}
