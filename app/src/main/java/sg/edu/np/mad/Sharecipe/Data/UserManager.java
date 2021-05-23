package sg.edu.np.mad.Sharecipe.Data;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import java9.util.concurrent.CompletableFuture;
import sg.edu.np.mad.Sharecipe.Models.Account;
import sg.edu.np.mad.Sharecipe.Models.User;
import sg.edu.np.mad.Sharecipe.web.SharecipeRequests;

public class UserManager {

    private static UserManager instance;

    public static UserManager getInstance(Context context) {
        if (instance == null) {
            instance = new UserManager(AccountManager.getInstance(context.getApplicationContext()));
        }
        return instance;
    }

    private final AccountManager accountManager;

    public UserManager(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    @NonNull
    public CompletableFuture<List<User>> searchUsers(String username) {
        if (!accountManager.isLoggedIn()) {
            return CompletableFuture.completedFuture(null);
        }
        CompletableFuture<List<User>> future = new CompletableFuture<>();
        SharecipeRequests.searchUsers(accountManager.getAccount().getAccessToken(), username)
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
        if (!accountManager.isLoggedIn()) {
            return CompletableFuture.completedFuture(null);
        }
        CompletableFuture<User> future = new CompletableFuture<>();
        Account account = accountManager.getAccount();
        SharecipeRequests.getUser(account.getAccessToken(), userId)
                .thenAccept(response -> {
                    JsonObject json = (JsonObject) SharecipeRequests.convertToJson(response);
                    future.complete(SharecipeRequests.convertToObject(json, User.class));
                });
        return future;
    }

    @NonNull
    public CompletableFuture<User> getLoggedInUser() {
        if (!accountManager.isLoggedIn()) {
            return CompletableFuture.completedFuture(null);
        }
        return getUser(accountManager.getAccount().getUserId());
    }
}
