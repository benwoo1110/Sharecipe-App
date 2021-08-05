package sg.edu.np.mad.Sharecipe.ui.common.listeners;

import android.view.View;

@FunctionalInterface
public interface OnSingleClickListener extends View.OnClickListener {

    void onSingleClick(View v);

    @Override
    default void onClick(View v) {
        v.setEnabled(false);
        onSingleClick(v);
    }
}
