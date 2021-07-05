package sg.edu.np.mad.Sharecipe.ui.create;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.github.dhaval2404.imagepicker.ImagePicker;

import java.util.ArrayList;

import sg.edu.np.mad.Sharecipe.R;

import static androidx.core.app.ActivityCompat.startActivityForResult;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesViewholder> {
    ArrayList<Bitmap> images;

    public ImagesAdapter(ArrayList<Bitmap> input) {this.images = input;}

    public ImagesViewholder onCreateViewHolder(ViewGroup parent, int viewtype){
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_images, parent, false);
        ImagesViewholder holder = new ImagesViewholder(item);

        if (viewtype == 0) {
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(item.getContext(), "Nice", Toast.LENGTH_SHORT).show(); //TO DO: Add image from user gallery on click
                }
            });
        }
        return holder;
    }

    public void onBindViewHolder(ImagesViewholder holder, int position) {
        if (position == 0) {
            // TO DO: Set image as default add button
            return;
        }

        Bitmap image = images.get(position - 1);
        // TO DO: Set images to view holder
    }

    @Override
    public int getItemCount() {
        return images.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? 0 : 1;
    }

}
