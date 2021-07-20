package sg.edu.np.mad.Sharecipe.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.gson.JsonElement;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import sg.edu.np.mad.Sharecipe.models.User;
import sg.edu.np.mad.Sharecipe.models.UserFollow;
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

        accountManager.getOrRefreshAccount().onSuccess(account -> {
            SharecipeRequests.searchUsers(account.getAccessToken(), username).onSuccessJson(future, (response, json) -> {
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
        }).onFailed(future).onError(future);

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
        User cacheUser = userCache.getIfPresent(userId);
        if (cacheUser != null) {
            return FutureDataResult.completed(cacheUser);
        }

        FutureDataResult<User> future = new FutureDataResult<>();

        accountManager.getOrRefreshAccount().onSuccess(account -> {
            SharecipeRequests.getUser(account.getAccessToken(), userId).onSuccessModel(future, User.class, (response, user) -> {
                userCache.put(userId, user);
                future.complete(new DataResult.Success<>(user));
            }).onFailed(future).onError(future);
        }).onFailed(future).onError(future);

        return future;
    }

    /**
     * Gets profile picture of a user.
     *
     * @param userId    Target user to get profile of.
     * @return Future result of image in bitmap format.
     */
    @NonNull
    public FutureDataResult<Bitmap> getProfileImage(int userId) {
        FutureDataResult<Bitmap> future = new FutureDataResult<>();

        accountManager.getOrRefreshAccount().onSuccess(account -> {
            SharecipeRequests.getUserProfileImage(account.getAccessToken(), userId).onSuccess(response -> {
                ResponseBody body = response.body();
                if (body == null) {
                    future.complete(new DataResult.Failed<>("No profile image data"));
                    return;
                }
                byte[] rawImageData;
                try {
                    rawImageData = body.bytes();
                } catch (IOException e) {
                    future.complete(new DataResult.Error<>(e));
                    return;
                }
                Bitmap bitmap = BitmapFactory.decodeByteArray(rawImageData,0, rawImageData.length);
                future.complete(new DataResult.Success<>(bitmap));
            }).onFailed(future).onError(future);
        }).onFailed(future).onError(future);

        return future;
    }

    /**
     * Gets all user ids that a given user is following.
     *
     * @param user  The target user.
     * @return Future result of follow data.
     */
    public FutureDataResult<List<UserFollow>> getFollows(User user) {
        FutureDataResult<List<UserFollow>> future = new FutureDataResult<>();

        accountManager.getOrRefreshAccount().onSuccess(account -> {
            SharecipeRequests.getUserFollows(account.getAccessToken(), account.getUserId()).onSuccessJson(future, (response, json) -> {
                List<UserFollow> followList = new ArrayList<>();
                for (JsonElement followData : json.getAsJsonArray()) {
                    followList.add(JsonUtils.convertToObject(followData, UserFollow.class));
                }
                future.complete(new DataResult.Success<>(followList));
            }).onFailed(future).onError(future);
        }).onFailed(future).onError(future);

        return future;
    }

    /**
     * Gets user data of logged in account.
     *
     * @return Future result of user data.
     */
    @NonNull
    public FutureDataResult<User> getAccountUser() {
        if (!accountManager.isLoggedIn()) {
            return FutureDataResult.completed(new DataResult.Failed<>("No account logged in!"));
        }
        return get(accountManager.getAccount().getUserId());
    }

    /**
     * Edit existing account user. Note you cannot edit other users.
     *
     * @param user  Updated user object.
     * @return Success status, no actual data returned.
     */
    @NonNull
    public FutureDataResult<Void> updateAccountUser(User user) {
        FutureDataResult<Void> future = new FutureDataResult<>();

        accountManager.getOrRefreshAccount().onSuccess(account -> {
            JsonElement userData = JsonUtils.convertToJson(user);
            SharecipeRequests.editUser(account.getAccessToken(), account.getUserId(), userData).onSuccess(response -> {
                future.complete(new DataResult.Success<>(null));
            }).onFailed(future).onError(future);
        }).onFailed(future).onError(future);

        return future;
    }

    /**
     * Sets profile picture for the user. Replace if user already have existing profile picture.
     *
     * @param imageFile Image to be set as profile picture.
     * @return Success status, no actual data returned.
     */
    @NonNull
    public FutureDataResult<Void> setAccountProfileImage(File imageFile) {
        if (imageFile == null || !imageFile.isFile()) {
            return FutureDataResult.completed(new DataResult.Failed<>("Image file not found."));
        }

        FutureDataResult<Void> future = new FutureDataResult<>();

        accountManager.getOrRefreshAccount().onSuccess(account -> {
            SharecipeRequests.setUserProfileImage(account.getAccessToken(), account.getUserId(), imageFile).onSuccess(response -> {
                future.complete(new DataResult.Success<>(null));
            }).onFailed(future).onError(future);
        }).onFailed(future).onError(future);

        return future;
    }

    /**
     * Follows a user.
     *
     * @param user  Target user to follow.
     * @return Success status, no actual data returned.
     */
    public FutureDataResult<Void> accountFollowUser(User user) {
        FutureDataResult<Void> future = new FutureDataResult<>();

        accountManager.getOrRefreshAccount().onSuccess(account -> {
            SharecipeRequests.putUserFollows(account.getAccessToken(), account.getUserId(), user.getUserId()).onSuccess(response -> {
                future.complete(new DataResult.Success<>(null));
            }).onFailed(future).onError(future);
        }).onFailed(future).onError(future);

        return future;
    }

    /**
     * Unfollows a user. Fails if account has not followed the user.
     *
     * @param user  Target user to unfollow.
     * @return Success status, no actual data returned.
     */
    public FutureDataResult<Void> accountUnfollowUser(User user) {
        FutureDataResult<Void> future = new FutureDataResult<>();

        accountManager.getOrRefreshAccount().onSuccess(account -> {
            SharecipeRequests.deleteUserFollows(account.getAccessToken(), account.getUserId(), user.getUserId()).onSuccess(response -> {
                future.complete(new DataResult.Success<>(null));
            }).onFailed(future).onError(future);
        }).onFailed(future).onError(future);

        return future;
    }
}
