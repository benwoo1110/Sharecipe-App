package sg.edu.np.mad.Sharecipe.ui.main.search;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.contants.IntentKeys;
import sg.edu.np.mad.Sharecipe.ui.main.profile.UserProfileActivity;

public class UserViewHolder extends RecyclerView.ViewHolder {

    final TextView name;
    final TextView bio;
    final ImageView profileImage;
    final ProgressBar progressBar;

    int userId;

    public UserViewHolder(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.textViewName);
        bio = itemView.findViewById(R.id.textViewBio);
        profileImage = itemView.findViewById(R.id.userProfileImage);
        progressBar = itemView.findViewById(R.id.imageLoadProgress);

        Context context = itemView.getContext();
        itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, UserProfileActivity.class);
            intent.putExtra(IntentKeys.USER_ID, userId);
            context.startActivity(intent);
        });
    }
}
