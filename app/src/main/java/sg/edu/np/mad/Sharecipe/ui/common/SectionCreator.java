package sg.edu.np.mad.Sharecipe.ui.common;

import android.view.View;

public interface SectionCreator {
    int getLayoutId();

    SectionViewHolder createViewHolder(View view);
}
