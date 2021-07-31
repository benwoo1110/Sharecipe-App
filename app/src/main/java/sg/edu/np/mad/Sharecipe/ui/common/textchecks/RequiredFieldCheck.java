package sg.edu.np.mad.Sharecipe.ui.common.textchecks;

import com.google.common.base.Strings;

public class RequiredFieldCheck extends AbstractChecker {

    @Override
    public boolean check() {
        String inputText = input.getEditText().getText().toString();
        if (Strings.isNullOrEmpty(inputText)) {
            input.setError("* This is a required field.");
            return false;
        } else {
            input.setError(null);
            return true;
        }
    }
}
