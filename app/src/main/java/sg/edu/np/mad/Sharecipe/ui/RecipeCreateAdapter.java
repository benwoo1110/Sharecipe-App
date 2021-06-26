package sg.edu.np.mad.Sharecipe.ui;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.jetbrains.annotations.NotNull;

public class RecipeCreateAdapter extends FragmentStateAdapter {

    private Context context;
    int totalTabs;

    public RecipeCreateAdapter(Context context, FragmentActivity fa, int totalTabs) {
        super(fa);
        this.context = context;
        this.totalTabs = totalTabs;
    }

    @NotNull
    @Override
    public Fragment createFragment(int position) {
        switch(position) {
            case 0:
                InformationFragment informationFragment = new InformationFragment();
                return informationFragment;

            case 1:
                StepsFragment stepsFragment = new StepsFragment();
                return stepsFragment;

            default:
                //TODO Return a error page instead of null
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return totalTabs;
    }

}
