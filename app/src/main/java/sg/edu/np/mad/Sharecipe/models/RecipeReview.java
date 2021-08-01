package sg.edu.np.mad.Sharecipe.models;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.Objects;

public class RecipeReview implements Serializable {

    @Expose
    private int recipeId;
    @Expose
    private int userId;
    @Expose
    private int rating;
    @Expose
    private String comment;

    public int getRecipeId() {
        return recipeId;
    }

    public int getUserId() {
        return userId;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeReview that = (RecipeReview) o;
        return recipeId == that.recipeId &&
                userId == that.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipeId, userId);
    }

    @Override
    public String toString() {
        return "RecipeReview{" +
                "recipeId=" + recipeId +
                ", userId=" + userId +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                '}';
    }
}
