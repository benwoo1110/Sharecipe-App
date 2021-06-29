package sg.edu.np.mad.Sharecipe.data;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import sg.edu.np.mad.Sharecipe.models.Account;
import sg.edu.np.mad.Sharecipe.models.User;
import sg.edu.np.mad.Sharecipe.utils.DataResult;
import sg.edu.np.mad.Sharecipe.utils.FutureDataResult;
import sg.edu.np.mad.Sharecipe.utils.JsonUtils;
import sg.edu.np.mad.Sharecipe.web.SharecipeRequests;

/**
 * Contains action for getting users data.
 */
public class UserManager {

    private static UserManager instance;

    /**
     * Gets common {@link UserManager} instance throughout the app.
     *
     * @param context   Application context for share preference loading.
     * @return The instance.
     */
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

    /**
     * Search for users that starts with given username string.
     *
     * @param username  Target username to search on.
     * @return Future result of search, with list of users from search result.
     */
    @NonNull
    public FutureDataResult<List<User>> search(String username) {
        FutureDataResult<List<User>> future = new FutureDataResult<>();
        if (!accountManager.isLoggedIn()) {
            future.complete(new DataResult.Failed<>("No account logged in!"));
            return future;
        }

        accountManager.getOrRefreshAccount().onSuccess(account -> {
            SharecipeRequests.searchUsers(accountManager.getAccount().getAccessToken(), username).thenAccept(response -> {
                JsonElement json = JsonUtils.convertToJson(response);
                if (!response.isSuccessful()) {
                    JsonElement message = json.getAsJsonObject().get("message");
                    future.complete(new DataResult.Failed<>(message != null ? message.getAsString() : "An unknown error occurred!"));
                    return;
                }
                List<User> userList = new ArrayList<>();
                for (JsonElement userData : json.getAsJsonArray()) {
                    userList.add(JsonUtils.convertToObject(userData, User.class));
                }
                future.complete(new DataResult.Success<>(userList));
            })
            .exceptionally(throwable -> {
                future.complete(new DataResult.Error<>(throwable));
                return null;
            });
        })
        .onFailed(reason -> future.complete(new DataResult.Failed<>(reason)))
        .onError(throwable -> future.complete(new DataResult.Error<>(throwable)));

        return future;
    }

    /**
     * Gets single user data.
     *
     * @param userId    Target user to get data of.
     * @return Future result of user data.
     */
    @NonNull
    public FutureDataResult<User> get(int userId) {
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
                    JsonObject json = (JsonObject) JsonUtils.convertToJson(response);
                    User user = JsonUtils.convertToObject(json, User.class);
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

    /**
     * Gets user data of logged in account.
     *
     * @return Future result of user data.
     */
    @NonNull
    public FutureDataResult<User> getLoggedIn() {
        if (!accountManager.isLoggedIn()) {
            FutureDataResult<User> future = new FutureDataResult<>();
            future.complete(new DataResult.Failed<>("No account logged in!"));
            return future;
        }
        return get(accountManager.getAccount().getUserId());
    }
}
