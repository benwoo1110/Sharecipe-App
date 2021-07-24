package sg.edu.np.mad.Sharecipe.models;

import android.graphics.Bitmap;

import com.google.gson.annotations.Expose;

import java.util.Date;

public class User {

    @Expose
    private int userId;
    @Expose
    private String username;
    @Expose
    private String bio;
    @Expose(serialize = false)
    private Date timeCreated;
    @Expose(serialize = false)
    private String profileImageId;

    @Deprecated
    private transient Bitmap profileImage;

    private User() { }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Date getTimeCreated() {
        return timeCreated;
    }

    public String getProfileImageId() {
        return profileImageId;
    }

    public Bitmap getProfileImage() {
        return profileImage;
    }

    @Deprecated
    public void setProfileImage(Bitmap profileImage) {
        this.profileImage = profileImage;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", bio='" + bio + '\'' +
                ", timeCreated=" + timeCreated +
                '}';
    }
}
