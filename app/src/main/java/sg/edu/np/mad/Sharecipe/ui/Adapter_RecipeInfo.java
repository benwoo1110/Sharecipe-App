package sg.edu.np.mad.Sharecipe.ui;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class Adapter_RecipeInfo extends FragmentStateAdapter {

    private Context myContext;
    int totalTabs;

    public Adapter_RecipeInfo(Context context, FragmentActivity fa, int totalTabs) {
        super(fa);
        myContext = context;
        this.totalTabs = totalTabs;
    }

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
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return totalTabs;
    }

}
