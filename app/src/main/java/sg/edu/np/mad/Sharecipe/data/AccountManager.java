package sg.edu.np.mad.Sharecipe.data;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import dev.haenara.bricksharepref.BrickSharedPreferences;
import sg.edu.np.mad.Sharecipe.models.Account;
import sg.edu.np.mad.Sharecipe.utils.DataResult;
import sg.edu.np.mad.Sharecipe.utils.FutureDataResult;
import sg.edu.np.mad.Sharecipe.utils.Interval;
import sg.edu.np.mad.Sharecipe.utils.JsonUtils;
import sg.edu.np.mad.Sharecipe.web.SharecipeRequests;

/**
 * Contains actions for an account.
 */
public class AccountManager {

    private static final String ACCOUNT_PREFS_NAME = "account";
    private static final int REFRESH_INTERVAL = 720000; // 12 minutes in milliseconds
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
    private final Interval refreshInterval;
    private Account account;

    private AccountManager(Context context) {
        this.context = context;
        this.refreshInterval = new Interval(REFRESH_INTERVAL);
        loadFromSharedPreferences();
    }

    /**
     * Create a new user account async.
     *
     * @param username  New account's name.
     * @param password  New account's password.
     * @return Future result of account creation with account data if succeed.
     */
    @NonNull
    public FutureDataResult<Account> register(String username, String password, String bio) {
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
        if (!isLoggedIn()) {
            return FutureDataResult.completed(new DataResult.Failed<>("No account logged in!"));
        }

        FutureDataResult<Account> future = new FutureDataResult<>();

        SharecipeRequests.accountTokenRefresh(account.getRefreshToken(), account.getUserId()).thenAccept(response -> {
            JsonObject json = (JsonObject) JsonUtils.convertToJson(response);
            JsonElement tokenElement = json != null ? json.get("access_token") : null;
            if (tokenElement == null) {
                future.complete(new DataResult.Failed<>("Account session expired!"));
                return;
            }
            account.setAccessToken(tokenElement.getAsString());
            future.complete(new DataResult.Success<>(account));
        })
        .exceptionally(throwable -> {
            future.complete(new DataResult.Error<>(throwable));
            return null;
        });

        return future;
    }

    /**
     * Logout from the current account. Tokens should be removed and invalidated.
     *
     * @return Success status, no actual data returned.
     */
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

    /**
     * Completely deletes the account. All data is removed and this is irreversible.
     *
     * @return Success status, no actual data returned.
     */
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
    @Nullable
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
        if (!isLoggedIn()) {
            return FutureDataResult.completed(new DataResult.Failed<>("No account logged in!"));
        }
        if (refreshInterval.update()) {
            return refresh();
        }
        return FutureDataResult.completed(account);
    }

    /**
     * Sets the logged in account.
     *
     * @param account   Target account object to set. Pass null to remove account.
     */
    private void setAccount(@Nullable Account account) {
        if (account == null || account.getUserId() == Integer.MIN_VALUE || account.getRefreshToken() == null) {
            this.account = null;
            clearSharedPreferences();
            return;
        }
        this.account = account;
        saveToSharedPreferences();
    }

    /**
     * Saves the refresh token to shared preferences to allow user to remain logged in.
     */
    private void saveToSharedPreferences() {
        if (account == null) {
            return;
        }
        openAccountSharedPreferences().edit()
                .putInt("userId", account.getUserId())
                .putString("refreshToken", account.getRefreshToken())
                .apply();
    }

    /**
     * Loads saved refresh token to auto login user.
     */
    private void loadFromSharedPreferences() {
        SharedPreferences data = openAccountSharedPreferences();
        setAccount(new Account(
                data.getInt("userId", Integer.MIN_VALUE),
                data.getString("refreshToken", null)
        ));
    }

    /**
     * Removes refresh token.
     */
    private void clearSharedPreferences() {
        openAccountSharedPreferences().edit()
                .clear()
                .apply();
    }

    /**
     * Create a secure encrypted shared preferences instance for account data.
     *
     * @return New shared preferences instance for use.
     */
    private SharedPreferences openAccountSharedPreferences() {
        return new BrickSharedPreferences(context, ACCOUNT_PREFS_NAME);
    }
}
