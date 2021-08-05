package sg.edu.np.mad.Sharecipe.ui.common;

import androidx.fragment.app.Fragment;

import sg.edu.np.mad.Sharecipe.contants.RunMode;
import sg.edu.np.mad.Sharecipe.ui.common.data.DataLoadable;
import sg.edu.np.mad.Sharecipe.ui.common.data.DataSaveable;

public abstract class DataActivity<T> extends BaseActivity implements DataLoadable<T>, DataSaveable<T> {

    @Override
    public void onDataLoaded(T t) {
    }

    @Override
    public void onDataSaving(T t) {
    }

    protected void callDataLoaded(T t) {
        onDataLoaded(t);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof DataLoadable) {
                try {
                    ((DataLoadable<T>) fragment).onDataLoaded(t);
                } catch (ClassCastException e) {
                    if (!RunMode.IS_PRODUCTION) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    protected void callDataSaving(T t) {
        onDataSaving(t);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof DataSaveable) {
                try {
                    ((DataSaveable<T>) fragment).onDataSaving(t);
                } catch (ClassCastException e) {
                    if (!RunMode.IS_PRODUCTION) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
