package sg.edu.np.mad.Sharecipe.ui.create.infomation;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.Sharecipe.R;

public class ImagesViewHolder extends RecyclerView.ViewHolder {

    final ImageView image;
    Bitmap imgBitmap;

    public ImagesViewHolder(View itemView){
        super(itemView);
        image = itemView.findViewById(R.id.addImage);
    }
}
