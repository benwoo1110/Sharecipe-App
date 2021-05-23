package sg.edu.np.mad.Sharecipe.ui.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DynamicFocusAppCompatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableDismissEditTextOnTouchOut();
    }

    @SuppressLint("ClickableViewAccessibility")
    protected void enableDismissEditTextOnTouchOut() {
        getWindow().getDecorView().getRootView().setOnTouchListener((v, event) -> {
            if (v instanceof EditText) {
                return false;
            }
            if (event.getAction() != MotionEvent.ACTION_DOWN) {
                return false;
            }
            View view = getCurrentFocus();
            if (!(view instanceof EditText)) {
                return false;
            }
            Rect outRect = new Rect();
            view.getGlobalVisibleRect(outRect);
            if (outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                return false;
            }
            view.clearFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            return false;
        });
    }
}
