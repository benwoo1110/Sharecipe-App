package sg.edu.np.mad.Sharecipe.ui.common.textchecks;

import com.google.android.material.textfield.TextInputLayout;

public class TextLengthChecker extends AbstractChecker {

    private final int minLength;
    private final int maxLength;
    private final boolean showCounter;

    public TextLengthChecker(int minLength, int maxLength) {
        this(minLength, maxLength, true);
    }

    public TextLengthChecker(int minLength, int maxLength, boolean showCounter) {
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.showCounter = showCounter;
    }

    @Override
    public void init(TextInputLayout input) {
        super.init(input);
        if (showCounter && maxLength < Integer.MAX_VALUE) {
            input.setCounterEnabled(true);
            input.setCounterMaxLength(maxLength);
        }
    }

    @Override
    public boolean check() {
        String inputText = getInputText();
        if (inputText.length() < minLength) {
            input.setError("Input needs to be at least " + minLength + " characters.");
            return false;
        } else if (inputText.length() > maxLength) {
            input.setError("Input can only have up to " + maxLength + " characters.");
            return false;
        } else {
            input.setError(null);
            return true;
        }
    }
}
