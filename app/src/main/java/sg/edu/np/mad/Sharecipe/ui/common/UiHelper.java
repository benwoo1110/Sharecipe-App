package sg.edu.np.mad.Sharecipe.ui.common;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import sg.edu.np.mad.Sharecipe.utils.DataResult;

public class UiHelper {

    public static void uiThread(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }

    public static void toastDataResult(Context context, DataResult<?> result) {
        uiThread(() -> Toast.makeText(context, result.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
