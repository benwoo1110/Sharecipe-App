package sg.edu.np.mad.Sharecipe.ui.view.reviews;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.contants.IntentKeys;
import sg.edu.np.mad.Sharecipe.models.RecipeReview;
import sg.edu.np.mad.Sharecipe.ui.main.profile.UserProfileActivity;

public class RecipeReviewViewholder extends RecyclerView.ViewHolder {


    final ImageView profilePic;
    final TextView username;
    final TextView rating;
    final TextView comment;
    final ProgressBar progressBar;

    RecipeReview review;

    public RecipeReviewViewholder(View itemView) {
        super(itemView);

        MaterialCardView card = itemView.findViewById(R.id.reviewCard);
        profilePic = itemView.findViewById(R.id.profilepicUser_review);
        username = itemView.findViewById(R.id.usernameUser_review);
        rating = itemView.findViewById(R.id.score_review);
        comment = itemView.findViewById(R.id.comment_review);
        progressBar = itemView.findViewById(R.id.imageLoadProgress);

        card.setOnClickListener(v -> {
            Intent intent = new Intent(itemView.getContext(), UserProfileActivity.class);
            intent.putExtra(IntentKeys.USER_ID, review.getUserId());
            itemView.getContext().startActivity(intent);
        });
    }
}
