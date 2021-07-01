package sg.edu.np.mad.Sharecipe.data;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;

import dev.haenara.bricksharepref.BrickSharedPreferences;
import sg.edu.np.mad.Sharecipe.models.Account;
import sg.edu.np.mad.Sharecipe.utils.DataResult;
import sg.edu.np.mad.Sharecipe.utils.FutureDataResult;
import sg.edu.np.mad.Sharecipe.utils.JsonUtils;
import sg.edu.np.mad.Sharecipe.web.SharecipeRequests;

/**
 * Contains actions for an account.
 */
public class AccountManager {

    private static final String ACCOUNT_PREFS_NAME = "account";
    private static final long REFRESH_INTERVAL = 720000; // 12 minutes in milliseconds
    private static AccountManager instance;

    /**
     * Gets common {@link AccountManager} instance throughout the app.
     *
     * @param context   Application context for share preference loading.
     * @return The instance.
     */
    public static AccountManager getInstance(Context context) {
        if (instance == null) {
            instance = new AccountManager(context.getApplicationContext());
        }
        return instance;
    }

    private final Context context;
    private Account account;
    private long lastRefresh;

    private AccountManager(Context context) {
        this.context = context;
        this.lastRefresh = 0;
        loadFromSharedPreference();
    }

    /**
     * Create a new user account async.
     *
     * @param username  New account's name.
     * @param password  New account's password.
     * @return Future result of account creation with account data if succeed.
     */
    @NonNull
    public FutureDataResult<Account> register(String username, String password, String bio, File imageFile) {
        FutureDataResult<Account> future = new FutureDataResult<>();
        SharecipeRequests.accountRegister(username, password, bio)
                .thenAccept(response -> {
                    JsonObject json = (JsonObject) JsonUtils.convertToJson(response);
                    if (!response.isSuccessful()) {
                        JsonElement message = json != null ? json.get("message") : null;
                        future.complete(new DataResult.Failed<>(message != null ? message.getAsString() : "An unknown error occurred!"));
                        return;
                    }
                    setAccount(JsonUtils.convertToObject(json, Account.class));
                    if (account == null) {
                        future.complete(new DataResult.Failed<>("Received invalid data. Failed to create account!"));
                    }
                    updateLastRefresh();
                    future.complete(new DataResult.Success<>(account));
                })
                .exceptionally(throwable -> {
                    future.complete(new DataResult.Error<>(throwable));
                    return null;
                });
        return future;
    }

    /**
     * Login to existing user account async.
     *
     * @param username  Account's username credentials.
     * @param password  Account's password credentials.
     * @return Future result of account login with account data if succeed.
     */
    @NonNull
    public FutureDataResult<Account> login(String username, String password) {
        FutureDataResult<Account> future = new FutureDataResult<>();
        SharecipeRequests.accountLogin(username, password)
                .thenAccept(response -> {
                    JsonObject json = (JsonObject) JsonUtils.convertToJson(response);
                    if (!response.isSuccessful()) {
                        JsonElement message = json.get("message");
                        future.complete(new DataResult.Failed<>(message != null ? message.getAsString() : "An unknown error occurred!"));
                        return;
                    }
                    setAccount(JsonUtils.convertToObject(json, Account.class));
                    if (account == null) {
                        future.complete(new DataResult.Failed<>("Received invalid data. Failed to login!"));
                    }
                    updateLastRefresh();
                    future.complete(new DataResult.Success<>(account));
                })
                .exceptionally(throwable -> {
                    future.complete(new DataResult.Error<>(throwable));
                    return null;
                });
        return future;
    }

    /**
     * Refreshes short live access token using the long live refresh token.
     *
     * @return Future result of refresh with account data if succeed.
     */
    @NonNull
    public FutureDataResult<Account> refresh() {
        FutureDataResult<Account> future = new FutureDataResult<>();
        if (!isLoggedIn()) {
            future.complete(new DataResult.Failed<>("No account logged in!"));
            return FutureDataResult.completed(new DataResult.Failed<>("No account logged in!"));
        }

        SharecipeRequests.accountTokenRefresh(account.getRefreshToken(), account.getUserId()).thenAccept(response -> {
            JsonObject json = (JsonObject) JsonUtils.convertToJson(response);
            JsonElement tokenElement = json != null ? json.get("access_token") : null;
            if (tokenElement == null) {
                future.complete(new DataResult.Failed<>("Account session expired!"));
                return;
            }
            account.setAccessToken(tokenElement.getAsString());
            updateLastRefresh();
            future.complete(new DataResult.Success<>(account));
        })
        .exceptionally(throwable -> {
            future.complete(new DataResult.Error<>(throwable));
            return null;
        });

        return future;
    }

    @NonNull
    public FutureDataResult<Void> logout() {
        FutureDataResult<Void> future = new FutureDataResult<>();
        if (!isLoggedIn()) {
            future.complete(new DataResult.Failed<>("No account logged in!"));
            return FutureDataResult.completed(new DataResult.Failed<>("No account logged in!"));
        }

        SharecipeRequests.accountLogout(account.getRefreshToken(), account.getUserId()).thenAccept(response -> {
            if (!response.isSuccessful()) {
                JsonObject json = (JsonObject) JsonUtils.convertToJson(response);
                JsonElement message = json != null ? json.get("message") : null;
                future.complete(new DataResult.Failed<>(message != null ? message.getAsString() : "An unknown error occurred!"));
                return;
            }
            setAccount(null);
            future.complete(new DataResult.Success<>(null));
        })
        .exceptionally(throwable -> {
            future.complete(new DataResult.Error<>(throwable));
            return null;
        });

        return future;
    }

    @NonNull
    public FutureDataResult<Void> delete() {
        return new FutureDataResult<>();
    }

    /**
     * Check if account is logged in.
     *
     * @return true if account logged in, else false.
     */
    public boolean isLoggedIn() {
        return account != null;
    }

    /**
     * Gets currently logged in account data.
     * Note: Use {@link AccountManager#getOrRefreshAccount()} to ensure token is up-to-date.
     *
     * @return Account object if logged in, else null.
     */
    public Account getAccount() {
        return account;
    }

    /**
     * Gets logged in account data and refresh access token if needed.
     *
     * @return Future result of account data.
     */
    @NonNull
    public FutureDataResult<Account> getOrRefreshAccount() {
        if (timeForRefresh()) {
            return refresh();
        }
        return FutureDataResult.completed(account);
    }

    private void setAccount(@Nullable Account account) {
        if (account == null || account.getUserId() == Integer.MIN_VALUE || account.getRefreshToken() == null) {
            this.account = null;
            clearSharedPreference();
            return;
        }
        this.account = account;
        saveToSharedPreference();
    }

    private void saveToSharedPreference() {
        if (account == null) {
            return;
        }
        openAccountSharedPreferences().edit()
                .putInt("userId", account.getUserId())
                .putString("refreshToken", account.getRefreshToken())
                .apply();
    }

    private void loadFromSharedPreference() {
        SharedPreferences data = openAccountSharedPreferences();
        setAccount(new Account(
                data.getInt("userId", Integer.MIN_VALUE),
                data.getString("refreshToken", null)
        ));
    }

    private void clearSharedPreference() {
        openAccountSharedPreferences().edit()
                .clear()
                .apply();
    }

    private SharedPreferences openAccountSharedPreferences() {
        return new BrickSharedPreferences(context, ACCOUNT_PREFS_NAME);
    }

    private boolean timeForRefresh() {
        return (System.currentTimeMillis() - lastRefresh) > REFRESH_INTERVAL;
    }

    private void updateLastRefresh() {
        lastRefresh = System.currentTimeMillis();
    }
}
