package sg.edu.np.mad.Sharecipe.Models;

import java.util.Objects;

public class User {
    private String username;
    private String bio;

    public User(String username, String bio) {
        this.username = username;
        this.bio = bio;
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
}
