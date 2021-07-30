package sg.edu.np.mad.Sharecipe.models;

import android.graphics.Bitmap;

import com.google.gson.annotations.Expose;

public class RecipeReviews {

    @Expose
    private String comment;

    @Expose
    private int rating;

    @Expose
    private String username;

    @Expose
    private Bitmap profilePic;

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

    public Bitmap getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(Bitmap profilePic) {
        this.profilePic = profilePic;
    }
}
