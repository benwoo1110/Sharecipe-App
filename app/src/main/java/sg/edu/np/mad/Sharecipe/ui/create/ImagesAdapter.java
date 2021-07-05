package sg.edu.np.mad.Sharecipe.ui.create;

import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sg.edu.np.mad.Sharecipe.R;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesViewholder> {
    ArrayList<Bitmap> images;

    public ImagesAdapter(ArrayList<Bitmap> input) {this.images = input;}

    public ImagesViewholder onCreateViewHolder(ViewGroup parent, int viewtype){
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_images, parent, false);
    }
}
