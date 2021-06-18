package sg.edu.np.mad.Sharecipe.models;

public class User {

    private int userId;
    private String username;
    private String bio;

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

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", bio='" + bio + '\'' +
                '}';
    }
}
