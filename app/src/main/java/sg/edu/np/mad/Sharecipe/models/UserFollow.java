package sg.edu.np.mad.Sharecipe.models;

import com.google.gson.annotations.Expose;

public class UserFollow {

    @Expose
    private int userId;
    @Expose
    private int followId;

    public int getUserId() {
        return userId;
    }

    public int getFollowId() {
        return followId;
    }

    @Override
    public String toString() {
        return "UserFollow{" +
                "userId=" + userId +
                ", followId=" + followId +
                '}';
    }
}
