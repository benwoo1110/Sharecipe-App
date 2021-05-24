package sg.edu.np.mad.Sharecipe.Data;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import java9.util.concurrent.CompletableFuture;
import sg.edu.np.mad.Sharecipe.Models.Account;
import sg.edu.np.mad.Sharecipe.Models.User;
import sg.edu.np.mad.Sharecipe.utils.DataResult;
import sg.edu.np.mad.Sharecipe.utils.FutureDataResult;
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
    private final Cache<Integer, User> userCache = CacheBuilder.newBuilder()
            .expireAfterAccess(10, TimeUnit.MINUTES)
            .build();

    public UserManager(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    @NonNull
    public FutureDataResult<List<User>> searchUsers(String username) {
        FutureDataResult<List<User>> future = new FutureDataResult<>();
        if (!accountManager.isLoggedIn()) {
            future.complete(new DataResult.Failed<>("No account logged in!"));
            return future;
        }
        SharecipeRequests.searchUsers(accountManager.getAccount().getAccessToken(), username)
                .thenAccept(response -> {
                    JsonElement json = SharecipeRequests.convertToJson(response);
                    if (!response.isSuccessful()) {
                        JsonElement message = json.getAsJsonObject().get("message");
                        future.complete(new DataResult.Failed<>(message != null ? message.getAsString() : "An unknown error occurred!"));
                        return;
                    }
                    List<User> userList = new ArrayList<>();
                    for (JsonElement userData : json.getAsJsonArray()) {
                        userList.add(SharecipeRequests.convertToObject(userData, User.class));
                    }
                    future.complete(new DataResult.Success<>(userList));
                })
                .exceptionally(throwable -> {
                    future.complete(new DataResult.Error<>(throwable));
                    return null;
                });
        return future;
    }

    @NonNull
    public FutureDataResult<User> getUser(int userId) {
        FutureDataResult<User> future = new FutureDataResult<>();
        if (!accountManager.isLoggedIn()) {
            future.complete(new DataResult.Failed<>("No account logged in!"));
            return future;
        }
        User cacheUser = userCache.getIfPresent(userId);
        if (cacheUser != null) {
            future.complete(new DataResult.Success<>(cacheUser));
            return future;
        }
        Account account = accountManager.getAccount();
        SharecipeRequests.getUser(account.getAccessToken(), userId)
                .thenAccept(response -> {
                    JsonObject json = (JsonObject) SharecipeRequests.convertToJson(response);
                    User user = SharecipeRequests.convertToObject(json, User.class);
                    if (user == null) {
                        future.complete(new DataResult.Failed<>("Invalid user data."));
                        return;
                    }
                    userCache.put(userId, user);
                    future.complete(new DataResult.Success<>(user));
                })
                .exceptionally(throwable -> {
                    future.complete(new DataResult.Error<>(throwable));
                    return null;
                });
        return future;
    }

    @NonNull
    public FutureDataResult<User> getLoggedInUser() {
        if (!accountManager.isLoggedIn()) {
            FutureDataResult<User> future = new FutureDataResult<>();
            future.complete(new DataResult.Failed<>("No account logged in!"));
            return future;
        }
        return getUser(accountManager.getAccount().getUserId());
    }
}
