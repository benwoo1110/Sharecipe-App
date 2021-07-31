package sg.edu.np.mad.Sharecipe.ui.common;

import android.view.View;

public interface OnSingleClickListener extends View.OnClickListener {

    void onSingleClick(View v);

    @Override
    default void onClick(View v) {
        v.setEnabled(false);
        onSingleClick(v);
    }
}
