package sg.edu.np.mad.Sharecipe.models;

import android.graphics.Bitmap;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class RecipeReview implements Serializable {

    @Expose
    private String comment;

    @Expose
    private int rating;

    @Expose
    private String username;

    @Expose
    private User user;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
