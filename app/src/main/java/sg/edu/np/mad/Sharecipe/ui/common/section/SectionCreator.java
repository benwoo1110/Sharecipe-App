package sg.edu.np.mad.Sharecipe.ui.common.section;

import android.view.View;

import androidx.annotation.NonNull;

public interface SectionCreator {
    int getLayoutId();

    @NonNull
    SectionViewHolder createViewHolder(View view);
}
