package sg.edu.np.mad.Sharecipe.ui.common.textchecks;

import com.google.android.material.textfield.TextInputLayout;

public class ConfirmMatchCheck extends AbstractChecker {

    private final TextInputLayout otherInput;

    public ConfirmMatchCheck(TextInputLayout otherInput) {
        this.otherInput = otherInput;
    }


    @Override
    public boolean check() {
        String inputText = getInputText();
        String otherText = getText(otherInput);
        if (!inputText.equals(otherText)) {
            input.setError("Password does not match.");
            return false;
        }
        input.setError(null);
        return true;
    }
}
