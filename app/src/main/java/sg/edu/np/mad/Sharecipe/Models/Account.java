package sg.edu.np.mad.Sharecipe.Models;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

public class Account {

    @Nullable
    public static Account fromJson(@Nullable JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }
        try {
            return new Account(
                    jsonObject.getInt("user_id"),
                    jsonObject.getString("access_token"),
                    jsonObject.getString("refresh_token")
            );
        } catch (JSONException e) {
            return null;
        }
    }

    private int userId;
    private String accessToken;
    private String refreshToken;

    public Account(int userId, String accessToken, String refreshToken) {
        this.userId = userId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
