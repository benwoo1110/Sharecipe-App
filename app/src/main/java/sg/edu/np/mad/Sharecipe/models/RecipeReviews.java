package sg.edu.np.mad.Sharecipe.models;

import com.google.gson.annotations.Expose;

public class RecipeReviews {

    @Expose
    private String comment;

    @Expose
    private int rating;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
