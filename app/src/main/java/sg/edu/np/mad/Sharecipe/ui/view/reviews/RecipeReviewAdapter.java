package sg.edu.np.mad.Sharecipe.ui.view.reviews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.models.RecipeReview;
import sg.edu.np.mad.Sharecipe.ui.App;

public class RecipeReviewAdapter extends RecyclerView.Adapter<RecipeReviewViewholder> {
    List<RecipeReview> reviews;

    public RecipeReviewAdapter(List<RecipeReview> reviews) {
        this.reviews = reviews;
    }

    @Override
    @NotNull
    public RecipeReviewViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_recipe_reviews, parent, false);
        RecipeReviewViewholder holder = new RecipeReviewViewholder(item);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NotNull RecipeReviewViewholder holder, int position) {
        RecipeReview review = reviews.get(position);
        App.getUserManager().getProfileImage(review.getUser()).onSuccess(bitmap -> holder.profilePic.setImageBitmap(bitmap));
        holder.username.setText(review.getUsername());
        holder.review.setText(review.getComment());
        holder.score.setText(String.valueOf(review.getRating()) + " /5");
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

}
