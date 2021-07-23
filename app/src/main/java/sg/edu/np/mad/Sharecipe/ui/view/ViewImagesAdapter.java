package sg.edu.np.mad.Sharecipe.ui.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.ui.create.infomation.ImagesViewHolder;

public class ViewImagesAdapter extends RecyclerView.Adapter<ViewImagesViewholder> {

    public ViewImagesAdapter() {}

    @Override
    @NotNull
    public ViewImagesViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_viewimages, parent, false);
        ViewImagesViewholder holder = new ViewImagesViewholder(item);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NotNull ViewImagesViewholder holder, int position) {
        // TODO: Bind stuff here
    }

    @Override
    public int getItemCount() {
        // TODO: Stuff
        int x = 1;
        return x;
    }
}
