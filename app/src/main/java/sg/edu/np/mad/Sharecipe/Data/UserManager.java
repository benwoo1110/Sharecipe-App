package sg.edu.np.mad.Sharecipe.Data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
                .thenApply(SharecipeRequests.DECODE_TO_JSON)
                .thenAccept(jsonObject -> {
                    account = Account.fromJson(jsonObject);
                    if (account == null) {
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
                    account = Account.fromJson(jsonObject);
                    if (account == null) {
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

    @NonNull
    public ActionResult deleteUser() {
        return ActionResult.GENERIC_ERROR;
    }

    @Nullable
    public Account getAccount() {
        return account;
    }
}
