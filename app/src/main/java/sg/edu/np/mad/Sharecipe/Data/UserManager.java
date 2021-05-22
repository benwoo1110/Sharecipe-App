package sg.edu.np.mad.Sharecipe.Data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.JsonObject;

import java9.util.concurrent.CompletableFuture;
import sg.edu.np.mad.Sharecipe.Models.Account;
import sg.edu.np.mad.Sharecipe.Models.User;
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

    private User loggedInUser;
    private Account account;

    private UserManager() {
    }

    @NonNull
    public CompletableFuture<ActionResult> register(String username, String password) {
        CompletableFuture<ActionResult> future = new CompletableFuture<>();
        SharecipeRequests.accountRegister(username, password)
                .thenAccept(response -> {
                    JsonObject json = SharecipeRequests.convertToJson(response);
                    if (!response.isSuccessful()) {
                        future.complete(new ActionResult.Failed(json.get("error").getAsString()));
                        return;
                    }
                    account = SharecipeRequests.convertToObject(json, Account.class);
                    if (account == null) {
                        future.complete(new ActionResult.Failed("Received invalid data. Failed to create account!"));
                    }
                    future.complete(new ActionResult.Success("Successfully create account!"));
                })
                .exceptionally(throwable -> {
                    future.complete(new ActionResult.Error(throwable));
                    return null;
                });
        return future;
    }

    @NonNull
    public CompletableFuture<ActionResult> login(String username, String password) {
        CompletableFuture<ActionResult> future = new CompletableFuture<>();
        SharecipeRequests.accountLogin(username, password)
                .thenAccept(response -> {
                    JsonObject json = SharecipeRequests.convertToJson(response);
                    if (!response.isSuccessful()) {
                        future.complete(new ActionResult.Failed(json.get("error").getAsString()));
                        return;
                    }
                    account = SharecipeRequests.convertToObject(json, Account.class);
                    if (account == null) {
                        future.complete(new ActionResult.Failed("Received invalid data. Failed to login!"));
                    }
                    future.complete(new ActionResult.Success("Successfully logged in!"));
                })
                .exceptionally(throwable -> {
                    future.complete(new ActionResult.Error(throwable));
                    return null;
                });
        return future;
    }

    @NonNull
    public ActionResult logout() {
        return ActionResult.GENERIC_FAILED;
    }

    @NonNull
    public ActionResult delete() {
        return ActionResult.GENERIC_FAILED;
    }

    @NonNull
    public CompletableFuture<User> getLoggedInUser() {
        if (account == null) {
            return CompletableFuture.completedFuture(null);
        }
        CompletableFuture<User> future = new CompletableFuture<>();
        if (loggedInUser != null) {
            future.complete(loggedInUser);
            return future;
        }
        SharecipeRequests.getUserData(account.getAccessToken(), account.getUserId())
                .thenAccept(response -> {
                    JsonObject json = SharecipeRequests.convertToJson(response);
                    loggedInUser = SharecipeRequests.convertToObject(json, User.class);
                    future.complete(loggedInUser);
                });
        return future;
    }

    @Nullable
    public Account getAccount() {
        return account;
    }
}
