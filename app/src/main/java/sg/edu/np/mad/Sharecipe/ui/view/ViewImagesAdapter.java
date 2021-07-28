package sg.edu.np.mad.Sharecipe.ui.view;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import sg.edu.np.mad.Sharecipe.R;

public class ViewImagesAdapter extends RecyclerView.Adapter<ViewImagesViewholder> {

    List<Bitmap> bitmapList;

    public ViewImagesAdapter() {
        bitmapList = new ArrayList<>();
    }

    @Override
    @NotNull
    public ViewImagesViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_viewimages, parent, false);
        return new ViewImagesViewholder(item);
    }

    @Override
    public void onBindViewHolder(@NotNull ViewImagesViewholder holder, int position) {
        holder.image.setImageBitmap(bitmapList.get(position));
    }

    @Override
    public int getItemCount() {
        return bitmapList.size();
    }

    public void setBitmapList(List<Bitmap> bitmapList) {
        this.bitmapList.clear();
        this.bitmapList.addAll(bitmapList);
        this.notifyDataSetChanged();
    }
}
