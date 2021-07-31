package sg.edu.np.mad.Sharecipe.ui.common.textchecks;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import java9.util.Lists;

public class CheckGroup {

    Map<TextInputLayout, List<Checker>> inputCheckers;

    public CheckGroup() {
        this.inputCheckers = new LinkedHashMap<>();
    }

    public CheckGroup add(TextInputLayout input, Checker...checkers) {
        List<Checker> focusChangeCheckers = new ArrayList<>();
        for (Checker checker : checkers) {
            checker.init(input);
            if (checker.doCheckOnFocusChange()) {
                focusChangeCheckers.add(checker);
            }
        }

        input.getEditText().setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                return;
            }
            for (Checker focusChangeChecker : focusChangeCheckers) {
                if (!focusChangeChecker.check()) {
                    return;
                }
            }
        });

        inputCheckers.put(input, Arrays.asList(checkers));

        return this;
    }

    public boolean checkAll() {
        return checkAll(true);
    }

    public boolean checkAll(boolean continueOnFail) {
        boolean passCheck = true;
        for (List<Checker> inputCheckers : inputCheckers.values()) {
            for (Checker checker : inputCheckers) {
                if (!checker.check()) {
                    passCheck = false;
                    if (!continueOnFail) {
                        return false;
                    }
                    break;
                }
            }
        }
        return passCheck;
    }
}
