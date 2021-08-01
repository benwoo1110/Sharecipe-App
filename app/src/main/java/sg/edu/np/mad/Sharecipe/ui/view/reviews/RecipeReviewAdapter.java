package sg.edu.np.mad.Sharecipe.ui.view.reviews;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import sg.edu.np.mad.Sharecipe.R;
import sg.edu.np.mad.Sharecipe.models.RecipeReview;
import sg.edu.np.mad.Sharecipe.ui.App;

public class RecipeReviewAdapter extends RecyclerView.Adapter<RecipeReviewViewholder> {

    private final List<RecipeReview> reviews;

    public RecipeReviewAdapter(List<RecipeReview> reviews) {
        this.reviews = reviews;
    }

    @Override
    @NotNull
    public RecipeReviewViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_recipe_reviews, parent, false);
        return new RecipeReviewViewholder(item);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NotNull RecipeReviewViewholder holder, int position) {
        RecipeReview review = reviews.get(position);
        holder.review.setText(review.getComment());
        holder.score.setText(review.getRating() + " / 5");

        App.getUserManager().get(review.getUserId()).onSuccess(user -> {
            new Handler(Looper.getMainLooper()).post(() -> {
                holder.username.setText(user.getUsername());
            });

            if (user.getProfileImageId() != null) {
                holder.profilePic.setImageBitmap(null);
                holder.progressBar.setVisibility(View.VISIBLE);
                App.getUserManager().getProfileImage(user).onSuccess(bitmap -> {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        holder.progressBar.setVisibility(View.GONE);
                        holder.profilePic.setImageBitmap(bitmap);
                    });
                });
            } else {
                holder.progressBar.setVisibility(View.GONE);
                holder.profilePic.setImageResource(R.drawable.ic_baseline_person_24);
            }
        });
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public void setReviews(List<RecipeReview> reviews) {
        this.reviews.clear();
        this.reviews.addAll(reviews);
        notifyDataSetChanged();
    }
}
