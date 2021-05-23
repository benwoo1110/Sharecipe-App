package sg.edu.np.mad.Sharecipe.Models;

public class User {
    private final int userId;
    private String username;
    private String bio;

    public User(int userId, String username, String bio) {
        this.userId = userId;
        this.username = username;
        this.bio = bio;
    }

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

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", bio='" + bio + '\'' +
                '}';
    }
}
