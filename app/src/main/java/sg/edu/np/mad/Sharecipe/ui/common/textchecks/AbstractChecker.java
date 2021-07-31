package sg.edu.np.mad.Sharecipe.ui.common.textchecks;

import com.google.android.material.textfield.TextInputLayout;

public abstract class AbstractChecker implements Checker {

    protected TextInputLayout input;

    protected AbstractChecker() { }

    @Override
    public void init(TextInputLayout input) {
        this.input = input;
    }
}
