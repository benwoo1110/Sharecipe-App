package sg.edu.np.mad.Sharecipe.Data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import sg.edu.np.mad.Sharecipe.Models.User;
import sg.edu.np.mad.Sharecipe.utils.ActionResult;

public class UserManager {

    private static UserManager instance;

    public UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    private User user;

    public UserManager() {
    }

    @NonNull
    public ActionResult register(String username, String password) {

        return ActionResult.GENERIC_SUCCESS;
    }

    @NonNull
    public ActionResult login(String username, String password) {
        return ActionResult.GENERIC_SUCCESS;
    }

    @NonNull
    public ActionResult updateUser() {
        return ActionResult.GENERIC_ERROR;
    }

    @Nullable
    public User getUser() {
        return user;
    }
}
