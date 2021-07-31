package sg.edu.np.mad.Sharecipe.ui.common.textchecks;

import com.google.android.material.textfield.TextInputLayout;

public interface Checker {

    void init(TextInputLayout input);

    boolean check();

    default boolean doCheckOnFocusChange() {
        return true;
    }
}
