package sg.edu.np.mad.Sharecipe.ui.common;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

import sg.edu.np.mad.Sharecipe.ui.ErrorFragment;

public class MenuStateAdapter extends FragmentStateAdapter {

    private final List<Class<? extends Fragment>> fragmentClasses = new ArrayList<>();
    private final List<Integer> menuIds = new ArrayList<>();

    public MenuStateAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public MenuStateAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public MenuStateAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        try {
            return fragmentClasses.get(position).newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            return new ErrorFragment();
        }
    }

    @Override
    public int getItemCount() {
        return fragmentClasses.size();
    }

    public MenuStateAdapter addFragmentClass(Class<? extends Fragment> fClass, @IdRes int menuId) {
        fragmentClasses.add(fClass);
        menuIds.add(menuId);
        return this;
    }

    public int getMenuPosition(@IdRes int menuId) {
        return menuIds.indexOf(menuId);
    }
}
