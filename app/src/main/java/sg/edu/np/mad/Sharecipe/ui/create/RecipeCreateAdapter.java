package sg.edu.np.mad.Sharecipe.ui.create;

import  androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.jetbrains.annotations.NotNull;

import sg.edu.np.mad.Sharecipe.models.Recipe;
import sg.edu.np.mad.Sharecipe.ui.ErrorFragment;

public class RecipeCreateAdapter extends FragmentStateAdapter {

    private final int totalTabs;
    private final Recipe recipe;

    public RecipeCreateAdapter(FragmentActivity fa, int totalTabs, Recipe recipe) {
        super(fa);
        this.totalTabs = totalTabs;
        this.recipe = recipe;
    }

    @NotNull
    @Override
    public Fragment createFragment(int position) {
        switch(position) {
            case 0:
                return new InformationFragment(recipe);
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
}
