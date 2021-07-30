package sg.edu.np.mad.Sharecipe.ui.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.models.RecipeReviews;

public class RecipeReviewAdapter extends RecyclerView.Adapter<RecipeReviewViewholder> {
    List<RecipeReviews> reviews;

    public RecipeReviewAdapter(List<RecipeReviews> reviews) {
        this.reviews = reviews;
    }

    @Override
    @NotNull
    public RecipeReviewViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_recipereviews, parent, false);
        RecipeReviewViewholder holder = new RecipeReviewViewholder(item);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NotNull RecipeReviewViewholder holder, int position) {
        RecipeReviews review = reviews.get(position);
        holder.profilePic.setImageBitmap(review.getProfilePic());
        holder.username.setText(review.getUsername());
        holder.review.setText(review.getComment());
        holder.score.setText(String.valueOf(review.getRating()) + " /5");
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

}
