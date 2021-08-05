package sg.edu.np.mad.Sharecipe.ui.common;

import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity class with some common behaviour
 */
public abstract class BaseActivity extends AppCompatActivity {



    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    hideSoftKeyBoard();
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    protected void hideSoftKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View currentFocus = getCurrentFocus();
        if(currentFocus != null && imm.isAcceptingText()) {
            currentFocus.clearFocus();
            imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
    }
}
