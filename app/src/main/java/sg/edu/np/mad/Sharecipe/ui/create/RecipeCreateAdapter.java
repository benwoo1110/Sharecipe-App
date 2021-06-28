package sg.edu.np.mad.Sharecipe.ui.create;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.jetbrains.annotations.NotNull;

import sg.edu.np.mad.Sharecipe.ui.ErrorFragment;

public class RecipeCreateAdapter extends FragmentStateAdapter {

    private final int totalTabs;

    public RecipeCreateAdapter(FragmentActivity fa, int totalTabs) {
        super(fa);
        this.totalTabs = totalTabs;
    }

    @NotNull
    @Override
    public Fragment createFragment(int position) {
        switch(position) {
            case 0:
                return new InformationFragment();
            case 1:
                return new StepsFragment();
            default:
                return new ErrorFragment();
        }
    }

    @Override
    public int getItemCount() {
        return totalTabs;
    }
}
