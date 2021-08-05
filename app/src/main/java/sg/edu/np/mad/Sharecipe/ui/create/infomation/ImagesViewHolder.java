package sg.edu.np.mad.Sharecipe.ui.create.infomation;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.Sharecipe.R;

public class ImagesViewHolder extends RecyclerView.ViewHolder {

    public final ImageView image;
    public BitmapOrUri imageFile;

    public ImagesViewHolder(View itemView){
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
