package sg.edu.np.mad.Sharecipe.ui.common;

import androidx.fragment.app.Fragment;

import sg.edu.np.mad.Sharecipe.contants.RunMode;
import sg.edu.np.mad.Sharecipe.ui.common.data.DataLoadable;
import sg.edu.np.mad.Sharecipe.ui.common.data.DataSaveable;

@SuppressWarnings("unchecked")
public abstract class DataActivity<T> extends BaseActivity {

    protected void callDataLoaded() {
        T data = supplyData();
        if (data == null) {
            return;
        }
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            tryCallOnDataLoaded(fragment, data);
        }
    }

    private void tryCallOnDataLoaded(Fragment fragment, T data) {
        if (fragment instanceof DataLoadable) {
            try {
                ((DataLoadable<T>) fragment).onDataLoaded(data);
            } catch (ClassCastException e) {
                if (!RunMode.IS_PRODUCTION) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected void callDataSaving() {
        T data = supplyData();
        if (data == null) {
            return;
        }
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            tryCallOnDataSaving(fragment, data);
        }
    }

    private void tryCallOnDataSaving(Fragment fragment, T data) {
        if (fragment instanceof DataSaveable) {
            try {
                ((DataSaveable<T>) fragment).onDataSaving(data);
            } catch (ClassCastException e) {
                if (!RunMode.IS_PRODUCTION) {
                    e.printStackTrace();
                }
            }
        }
    }

    //TODO Maybe use Optional?
    protected abstract T supplyData();
}
