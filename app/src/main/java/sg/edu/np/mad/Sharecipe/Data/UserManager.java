package sg.edu.np.mad.Sharecipe.Data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java9.util.concurrent.CompletableFuture;
import sg.edu.np.mad.Sharecipe.Models.UserAuth;
import sg.edu.np.mad.Sharecipe.utils.ActionResult;
import sg.edu.np.mad.Sharecipe.web.SharecipeRequests;

public class UserManager {

    private static UserManager instance;

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    private UserAuth userAuth;

    private UserManager() {
    }

    @NonNull
    public CompletableFuture<ActionResult> register(String username, String password) {
        CompletableFuture<ActionResult> future = new CompletableFuture<>();
        SharecipeRequests.accountRegister(username, password)
                .thenApply(SharecipeRequests.DECODE_TO_JSON)
                .thenAccept(jsonObject -> {
                    userAuth = UserAuth.fromJson(jsonObject);
                    if (userAuth == null) {
                        future.complete(ActionResult.GENERIC_ERROR);
                    }
                    future.complete(ActionResult.GENERIC_SUCCESS);
                })
                .exceptionally(throwable -> {
                    future.complete(ActionResult.GENERIC_ERROR);
                    return null;
                });
        return future;
    }

    @NonNull
    public CompletableFuture<ActionResult> login(String username, String password) {
        CompletableFuture<ActionResult> future = new CompletableFuture<>();
        SharecipeRequests.accountLogin(username, password)
                .thenApply(SharecipeRequests.DECODE_TO_JSON)
                .thenAccept(jsonObject -> {
                    userAuth = UserAuth.fromJson(jsonObject);
                    if (userAuth == null) {
                        future.complete(ActionResult.GENERIC_ERROR);
                    }
                    future.complete(ActionResult.GENERIC_SUCCESS);
                })
                .exceptionally(throwable -> {
                    future.complete(ActionResult.GENERIC_ERROR);
                    return null;
                });
        return future;
    }

    @NonNull
    public ActionResult updateUser() {
        return ActionResult.GENERIC_ERROR;
    }

    @Nullable
    public UserAuth getUserAuth() {
        return userAuth;
    }
}
