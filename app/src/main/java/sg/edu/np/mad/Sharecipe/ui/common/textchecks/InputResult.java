package sg.edu.np.mad.Sharecipe.ui.common.textchecks;

import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

public class InputResult {

    boolean passedAllChecks;
    Map<TextInputLayout, String> inputMap;

    InputResult() {
        passedAllChecks = false;
        inputMap = new HashMap<>();
    }

    public boolean passedAllChecks() {
        return passedAllChecks;
    }

    public String get(TextInputLayout inputLayout) {
        return inputMap.get(inputLayout);
    }
}
