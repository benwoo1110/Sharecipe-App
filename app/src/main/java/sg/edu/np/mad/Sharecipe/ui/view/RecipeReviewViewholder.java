package sg.edu.np.mad.Sharecipe.ui.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.Sharecipe.R;

public class RecipeReviewViewholder extends RecyclerView.ViewHolder {

    ImageView profilePic;
    TextView username;
    TextView review;
    TextView score;


    public RecipeReviewViewholder(View itemView) {
        super(itemView);

        profilePic = itemView.findViewById(R.id.profilepicUser_review);
        username = itemView.findViewById(R.id.usernameUser_review);
        review = itemView.findViewById(R.id.comment_review);
        score = itemView.findViewById(R.id.score_review);
    }
}
