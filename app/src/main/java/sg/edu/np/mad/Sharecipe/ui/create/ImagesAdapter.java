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
import com.google.android.material.transition.Hold;

import java.util.ArrayList;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import sg.edu.np.mad.Sharecipe.R;

import static androidx.core.app.ActivityCompat.startActivityForResult;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesViewholder> {

    Activity activity;
    ArrayList<Uri> images;

    public ImagesAdapter(Activity activity, ArrayList<Uri> input) {
        this.activity = activity;
        this.images = input;
    }

    public ImagesViewholder onCreateViewHolder(ViewGroup parent, int viewtype){
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_images, parent, false);
        ImagesViewholder holder = new ImagesViewholder(item);

        if (viewtype == 0) {
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(item.getContext(), "Nice", Toast.LENGTH_SHORT).show(); //TODO: Add image from user gallery on click
                    ImagePicker.with(activity)
                            .crop()	// Crop image(Optional), Check Customization for more option
                            .compress(1024)	// Final image size will be less than 1 MB
                            .maxResultSize(500, 500) // Final image resolution will be less than 500x500
                            .start();
                }
            });
        }
        return holder;
    }

    public void onBindViewHolder(ImagesViewholder holder, int position) {
        if (position == 0) {
            // TODO: Set image as default add button
            return;
        }

        Uri image = images.get(position - 1);
        holder.image.setImageURI(image);
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
