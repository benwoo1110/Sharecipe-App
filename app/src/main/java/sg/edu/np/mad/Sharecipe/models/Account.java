package sg.edu.np.mad.Sharecipe.models;

import com.google.gson.annotations.Expose;

import org.jetbrains.annotations.NotNull;

public class Account {

    @Expose
    private final int userId;
    @Expose
    private final String refreshToken;
    @Expose
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
