package sg.edu.np.mad.Sharecipe.ui;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.Sharecipe.R;

public class UserViewHolder extends RecyclerView.ViewHolder {

    final TextView name;
    final TextView bio;

    public UserViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.textViewName);
        bio = itemView.findViewById(R.id.textViewBio);
    }
}
