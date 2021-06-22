package sg.edu.np.mad.Sharecipe.ui;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class Adapter_RecipeInfo extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;

    public Adapter_RecipeInfo(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    @Override
    public Fragment getItem(int position) {
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
    public int getCount() {
        return totalTabs;
    }

}
