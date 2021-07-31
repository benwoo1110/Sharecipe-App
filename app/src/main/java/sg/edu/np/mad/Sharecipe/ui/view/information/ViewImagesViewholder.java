package sg.edu.np.mad.Sharecipe.ui.view.information;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.Sharecipe.R;

public class ViewImagesViewholder extends RecyclerView.ViewHolder {

    final ImageView image;
    Bitmap imgBitmap;

    public ViewImagesViewholder(View itemView) {
        super(itemView);
        image = itemView.findViewById(R.id.viewImage);

        // Ensure its centered properly
        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        int displayWidth = displayMetrics.widthPixels;
        int spacing = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 56, displayMetrics);
        int imageSpan = (displayWidth - spacing) / 2;
        image.getLayoutParams().width = imageSpan;
        image.getLayoutParams().height = imageSpan;
    }
}
