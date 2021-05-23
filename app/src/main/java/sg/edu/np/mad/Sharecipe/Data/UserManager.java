package sg.edu.np.mad.Sharecipe.Data;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import java9.util.concurrent.CompletableFuture;
import sg.edu.np.mad.Sharecipe.Models.Account;
import sg.edu.np.mad.Sharecipe.Models.User;
import sg.edu.np.mad.Sharecipe.utils.ActionResult;
import sg.edu.np.mad.Sharecipe.web.SharecipeRequests;

public class UserManager {

    private static UserManager instance;

    public static UserManager getInstance(Context context) {
        if (instance == null) {
            instance = new UserManager(context.getApplicationContext());
        }
        return instance;
    }

    private final Context context;
    private User loggedInUser;
    private Account account;

    private UserManager(Context context) {
        this.context = context;
        loadFromSharedPreference();
    }

    @NonNull
    public CompletableFuture<ActionResult> register(String username, String password) {
        CompletableFuture<ActionResult> future = new CompletableFuture<>();
        SharecipeRequests.accountRegister(username, password)
                .thenAccept(response -> {
                    JsonObject json = (JsonObject) SharecipeRequests.convertToJson(response);
                    if (!response.isSuccessful()) {
                        JsonElement message = json != null ? json.get("message") : null;
                        future.complete(new ActionResult.Failed(message != null ? message.getAsString() : "An unknown error occurred!"));
                        return;
                    }
                    setAccount(SharecipeRequests.convertToObject(json, Account.class));
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
                    JsonObject json = (JsonObject) SharecipeRequests.convertToJson(response);
                    if (!response.isSuccessful()) {
                        JsonElement message = json.get("message");
                        future.complete(new ActionResult.Failed(message != null ? message.getAsString() : "An unknown error occurred!"));
                        return;
                    }
                    setAccount(SharecipeRequests.convertToObject(json, Account.class));
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
    public ActionResult refresh() {
        return ActionResult.GENERIC_FAILED;
    }

    @NonNull
    public ActionResult logout() {
        return ActionResult.GENERIC_FAILED;
    }

    @NonNull
    public ActionResult delete() {
        return ActionResult.GENERIC_FAILED;
    }

    private void setAccount(@Nullable Account account) {
        if (account == null || account.getUserId() == Integer.MIN_VALUE || account.getRefreshToken() == null) {
            this.account = null;
            return;
        }
        this.account = account;
        saveToSharedPreference();
    }

    private void saveToSharedPreference() {
        if (account == null) {
            return;
        }
        context.getSharedPreferences("account", Context.MODE_PRIVATE)
                .edit()
                .putInt("userId", account.getUserId())
                .putString("refreshToken", account.getRefreshToken())
                .apply();
    }

    private void loadFromSharedPreference() {
        SharedPreferences data = context.getSharedPreferences("account", Context.MODE_PRIVATE);
        setAccount(new Account(data.getInt("userId", Integer.MIN_VALUE), data.getString("refreshToken", null)));
    }

    private void clearSharedPreference() {
        context.getSharedPreferences("auth", Context.MODE_PRIVATE)
                .edit()
                .clear()
                .apply();
    }

    @NonNull
    public CompletableFuture<List<User>> searchUsers(String username) {
        if (account == null) {
            return CompletableFuture.completedFuture(null);
        }
        CompletableFuture<List<User>> future = new CompletableFuture<>();
        SharecipeRequests.searchUsers(account.getAccessToken(), username)
                .thenAccept(response -> {
                    JsonElement json = SharecipeRequests.convertToJson(response);
                    if (!response.isSuccessful()) {
                        future.complete(null);
                        return;
                    }
                    List<User> userList = new ArrayList<>();
                    for (JsonElement userData : json.getAsJsonArray()) {
                        userList.add(SharecipeRequests.convertToObject(userData, User.class));
                    }
                    future.complete(userList);
                });
        return future;
    }

    @NonNull
    public CompletableFuture<User> getUser(int userId) {
        if (account == null) {
            return CompletableFuture.completedFuture(null);
        }
        CompletableFuture<User> future = new CompletableFuture<>();
        if (loggedInUser != null) {
            future.complete(loggedInUser);
            return future;
        }
        SharecipeRequests.getUser(account.getAccessToken(), account.getUserId())
                .thenAccept(response -> {
                    JsonObject json = (JsonObject) SharecipeRequests.convertToJson(response);
                    future.complete(SharecipeRequests.convertToObject(json, User.class));
                });
        return future;
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
        SharecipeRequests.getUser(account.getAccessToken(), account.getUserId())
                .thenAccept(response -> {
                    JsonObject json = (JsonObject) SharecipeRequests.convertToJson(response);
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
