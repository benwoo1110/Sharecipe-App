package sg.edu.np.mad.Sharecipe.ui.create;

import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.Sharecipe.R;

public class ImagesViewholder extends RecyclerView.ViewHolder {

    ImageView image;

    public ImagesViewholder(View itemView){
        super(itemView);
        image = itemView.findViewById(R.id.addImage);
    }
}
