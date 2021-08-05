package sg.edu.np.mad.Sharecipe.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import com.google.common.base.Strings;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.gson.JsonElement;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import sg.edu.np.mad.Sharecipe.models.BooleanState;
import sg.edu.np.mad.Sharecipe.models.Stats;
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
            instance = new UserManager(AccountManager.getInstance(context.getApplicationContext()), BitmapCacheManager.getInstance());
        }
        return instance;
    }

    private final AccountManager accountManager;
    private final BitmapCacheManager bitmapCacheManager;
    private final Cache<Integer, User> userCache = CacheBuilder.newBuilder()
            .expireAfterAccess(5, TimeUnit.MINUTES)
            .maximumSize(500)
            .build();

    public UserManager(AccountManager accountManager, BitmapCacheManager bitmapCacheManager) {
        this.accountManager = accountManager;
        this.bitmapCacheManager = bitmapCacheManager;
    }

    /**
     * Get all users.
     *
     * @return Future result of with list of users.
     */
    @NonNull
    public FutureDataResult<List<User>> getAll() {
        FutureDataResult<List<User>> future = new FutureDataResult<>();

        accountManager.getOrRefreshAccount().onSuccess(account -> {
            SharecipeRequests.getUsers(account.getAccessToken(), null).onSuccessJson(future, (response, json) -> {
                List<User> userList = new ArrayList<>();
                for (JsonElement userData : json.getAsJsonArray()) {
                    User user = JsonUtils.convertToObject(userData, User.class);
                    userList.add(user);
                    userCache.put(user.getUserId(), user);
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
                userCache.put(user.getUserId(), user);
                future.complete(new DataResult.Success<>(user));
            }).onFailed(future).onError(future);
        }).onFailed(future).onError(future);

        return future;
    }

    @NonNull
    public FutureDataResult<List<Stats>> getStats(User user) {
        FutureDataResult<List<Stats>> future = new FutureDataResult<>();

        accountManager.getOrRefreshAccount().onSuccess(account -> {
            SharecipeRequests.getUserStats(account.getAccessToken(), user.getUserId()).onSuccessJson(future, (response, json) -> {
                List<Stats> statsList = new ArrayList<>();
                for (JsonElement statsData : json.getAsJsonArray()) {
                    statsList.add(JsonUtils.convertToObject(statsData, Stats.class));
                }
                future.complete(new DataResult.Success<>(statsList));
            });
        }).onFailed(future).onError(future);

        return future;
    }

    /**
     * Gets profile picture of a user.
     *
     * @param user    Target user to get profile of.
     * @return Future result of image in bitmap format.
     */
    @NonNull
    public FutureDataResult<Bitmap> getProfileImage(User user) {
        if (Strings.isNullOrEmpty(user.getProfileImageId())) {
            return FutureDataResult.completed(new DataResult.Failed<>("User does not have profile image."));
        }

        Bitmap cacheImage = bitmapCacheManager.getBitmapFromMemCache(user.getProfileImageId());
        if (cacheImage != null) {
            return FutureDataResult.completed(cacheImage);
        }

        FutureDataResult<Bitmap> future = new FutureDataResult<>();

        accountManager.getOrRefreshAccount().onSuccess(account -> {
            SharecipeRequests.getUserProfileImage(account.getAccessToken(), user.getUserId()).onSuccess(response -> {
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
                bitmapCacheManager.addBitmapToMemoryCache(user.getProfileImageId(), bitmap);
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
    public FutureDataResult<List<User>> getFollows(User user) {
        FutureDataResult<List<User>> future = new FutureDataResult<>();

        accountManager.getOrRefreshAccount().onSuccess(account -> {
            SharecipeRequests.getUserFollows(account.getAccessToken(), user.getUserId()).onSuccessJson(future, (response, json) -> {
                List<User> userList = new ArrayList<>();
                for (JsonElement userData : json.getAsJsonArray()) {
                    User followUser = JsonUtils.convertToObject(userData, User.class);
                    userList.add(followUser);
                    userCache.put(followUser.getUserId(), followUser);
                }
                future.complete(new DataResult.Success<>(userList));
            }).onFailed(future).onError(future);
        }).onFailed(future).onError(future);

        return future;
    }

    /**
     * Gets all user ids that is following the given user.
     *
     * @param user  The target user.
     * @return Future result of followers data.
     */
    public FutureDataResult<List<User>> getFollowers(User user) {
        FutureDataResult<List<User>> future = new FutureDataResult<>();

        accountManager.getOrRefreshAccount().onSuccess(account -> {
            SharecipeRequests.getUserFollowers(account.getAccessToken(), user.getUserId()).onSuccessJson(future, (response, json) -> {
                List<User> userList = new ArrayList<>();
                for (JsonElement userData : json.getAsJsonArray()) {
                    User followUser = JsonUtils.convertToObject(userData, User.class);
                    userList.add(followUser);
                    userCache.put(followUser.getUserId(), followUser);
                }
                future.complete(new DataResult.Success<>(userList));
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
            SharecipeRequests.patchUser(account.getAccessToken(), account.getUserId(), userData).onSuccess(response -> {
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
            SharecipeRequests.putUserProfileImage(account.getAccessToken(), account.getUserId(), imageFile).onSuccess(response -> {
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
            SharecipeRequests.postUserFollowUser(account.getAccessToken(), account.getUserId(), user.getUserId()).onSuccess(response -> {
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
            SharecipeRequests.deleteUserFollowUser(account.getAccessToken(), account.getUserId(), user.getUserId()).onSuccess(response -> {
                future.complete(new DataResult.Success<>(null));
            }).onFailed(future).onError(future);
        }).onFailed(future).onError(future);

        return future;
    }

    /**
     * Check if the account is following a given user.
     *
     * @param user  Target user to check.
     * @return Future result of boolean state.
     */
    public FutureDataResult<BooleanState> checkIfAccountFollow(User user) {
        FutureDataResult<BooleanState> future = new FutureDataResult<>();

        accountManager.getOrRefreshAccount().onSuccess(account -> {
            SharecipeRequests.getUserFollowUser(account.getAccessToken(), account.getUserId(), user.getUserId()).onSuccessModel(future, BooleanState.class, (response, booleanState) -> {
                future.complete(new DataResult.Success<>(booleanState));
            }).onFailed(future).onError(future);
        }).onFailed(future).onError(future);

        return future;
    }

    public void invalidateUser(User user) {
        userCache.invalidate(user.getUserId());
    }

    public void invalidateAll() {
        userCache.invalidateAll();
    }

    void addToCache(User user) {
        userCache.put(user.getUserId(), user);
    }
}
