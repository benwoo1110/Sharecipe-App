package sg.edu.np.mad.Sharecipe.ui.view;

import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.Sharecipe.R;

public class ViewImagesViewholder extends RecyclerView.ViewHolder {

    final ImageView image;

    public ViewImagesViewholder(View itemView) {
        super(itemView);
        image = itemView.findViewById(R.id.viewImage);
    }
}
