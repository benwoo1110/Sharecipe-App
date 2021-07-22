package sg.edu.np.mad.Sharecipe.ui.common;

import com.google.android.material.tabs.TabLayout;

public interface OnTabSelectedListener extends TabLayout.OnTabSelectedListener {

    @Override
    void onTabSelected(TabLayout.Tab tab);

    @Override
    default void onTabUnselected(TabLayout.Tab tab) { }

    @Override
    default void onTabReselected(TabLayout.Tab tab) { }
}
