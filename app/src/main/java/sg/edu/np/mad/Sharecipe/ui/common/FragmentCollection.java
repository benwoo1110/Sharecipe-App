package sg.edu.np.mad.Sharecipe.ui.common;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.HashMap;
import java.util.Map;

import java9.util.Optional;
import sg.edu.np.mad.Sharecipe.ui.ErrorFragment;

public class FragmentCollection {

    private final Map<Class, Fragment> fragmentMap;

    public FragmentCollection() {
        fragmentMap = new HashMap<>();
    }

    @NonNull
    public Fragment getOrLoad(Class<? extends Fragment> fragmentClass) {
        Fragment fragment = fragmentMap.get(fragmentClass);
        if (fragment != null) {
            return fragmentMap.get(fragmentClass);
        }

        try {
            fragment = fragmentClass.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            return new ErrorFragment();
        }

        fragmentMap.put(fragmentClass, fragment);
        return fragment;
    }
}
