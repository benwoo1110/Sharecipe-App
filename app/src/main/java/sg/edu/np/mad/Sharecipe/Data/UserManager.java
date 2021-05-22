package sg.edu.np.mad.Sharecipe.Data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONObject;

import java9.util.concurrent.CompletableFuture;
import sg.edu.np.mad.Sharecipe.Models.Account;
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

    private Account account;

    private UserManager() {
    }

    @NonNull
    public CompletableFuture<ActionResult> register(String username, String password) {
        CompletableFuture<ActionResult> future = new CompletableFuture<>();
        SharecipeRequests.accountRegister(username, password)
                .thenAccept(response -> {
                    JSONObject jsonObject = SharecipeRequests.DECODE_TO_JSON.apply(response);
                    if (!response.isSuccessful()) {
                        future.complete(new ActionResult.Failed(jsonObject.optString("error", "Failed!")));
                        return;
                    }
                    account = Account.fromJson(jsonObject);
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
                    JSONObject jsonObject = SharecipeRequests.DECODE_TO_JSON.apply(response);
                    if (!response.isSuccessful()) {
                        future.complete(new ActionResult.Failed(jsonObject.optString("error", "Failed!")));
                        return;
                    }
                    account = Account.fromJson(jsonObject);
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
    public ActionResult updateUser() {
        return ActionResult.GENERIC_FAILED;
    }

    @NonNull
    public ActionResult deleteUser() {
        return ActionResult.GENERIC_FAILED;
    }

    @Nullable
    public Account getAccount() {
        return account;
    }
}
