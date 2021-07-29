package sg.edu.np.mad.Sharecipe.ui.main.discover;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.Sharecipe.R;

public class SmallCardViewHolder extends RecyclerView.ViewHolder {

    final TextView title;
    final ImageView image;

    public SmallCardViewHolder(@NonNull View itemView) {
        super(itemView);

        title = itemView.findViewById(R.id.discoverSmallTitle);
        image = itemView.findViewById(R.id.discoverSmallImage);
    }
}
