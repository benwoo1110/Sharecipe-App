package sg.edu.np.mad.Sharecipe.ui.common.textchecks;

public class TextLengthChecker extends AbstractChecker {

    private final int minLength;
    private final int maxLength;

    public TextLengthChecker(int minLength, int maxLength) {
        this.minLength = minLength;
        this.maxLength = maxLength;
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
