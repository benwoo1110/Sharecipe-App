package sg.edu.np.mad.Sharecipe.Models;

import org.jetbrains.annotations.NotNull;

public class Account {
    private final int userId;
    private final String refreshToken;
    private String accessToken;

    public Account(int userId, String refreshToken) {
        this(userId, refreshToken, null);
    }

    public Account(int userId, String refreshToken, String accessToken) {
        this.userId = userId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public int getUserId() {
        return userId;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @NotNull
    @Override
    public String toString() {
        return "Account{" +
                "userId=" + userId +
                ", refreshToken='" + refreshToken + '\'' +
                ", accessToken='" + accessToken + '\'' +
                '}';
    }
}
