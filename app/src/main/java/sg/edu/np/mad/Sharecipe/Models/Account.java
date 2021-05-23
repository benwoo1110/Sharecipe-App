package sg.edu.np.mad.Sharecipe.Models;

public class Account {
    private final int userId;
    private String accessToken;
    private final String refreshToken;

    public Account(int userId, String refreshToken) {
        this.userId = userId;
        this.refreshToken = refreshToken;
    }

    public Account(int userId, String accessToken, String refreshToken) {
        this.userId = userId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public int getUserId() {
        return userId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    @Override
    public String toString() {
        return "Account{" +
                "userId=" + userId +
                ", accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                '}';
    }
}
