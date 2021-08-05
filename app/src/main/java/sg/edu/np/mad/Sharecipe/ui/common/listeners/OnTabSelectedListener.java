package sg.edu.np.mad.Sharecipe.ui.common.listeners;

import com.google.android.material.tabs.TabLayout;

@FunctionalInterface
public interface OnTabSelectedListener extends TabLayout.OnTabSelectedListener {

    @Override
    void onTabSelected(TabLayout.Tab tab);

    @Override
    default void onTabUnselected(TabLayout.Tab tab) { }

    @Override
    default void onTabReselected(TabLayout.Tab tab) { }
}
