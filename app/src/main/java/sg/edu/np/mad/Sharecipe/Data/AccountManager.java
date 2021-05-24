package sg.edu.np.mad.Sharecipe.Data;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import sg.edu.np.mad.Sharecipe.Models.Account;
import sg.edu.np.mad.Sharecipe.Models.User;
import sg.edu.np.mad.Sharecipe.utils.DataResult;
import sg.edu.np.mad.Sharecipe.utils.FutureDataResult;
import sg.edu.np.mad.Sharecipe.utils.JsonUtils;
import sg.edu.np.mad.Sharecipe.web.SharecipeRequests;

/**
 * Contains action for an account.
 */
public class AccountManager {

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
    private User loggedInUser;
    private Account account;

    private AccountManager(Context context) {
        this.context = context;
        // loadFromSharedPreference();
    }

    /**
     * Create a new user account async.
     *
     * @param username  New account's name.
     * @param password  New account's password.
     * @return Future result of account creation with account data if succeed.
     */
    @NonNull
    public FutureDataResult<Account> register(String username, String password) {
        FutureDataResult<Account> future = new FutureDataResult<>();
        SharecipeRequests.accountRegister(username, password)
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
        FutureDataResult<Account> future = new FutureDataResult<>();
        if (account == null) {
            future.complete(new DataResult.Failed<>("No account logged in!"));
            return future;
        }
        SharecipeRequests.accountTokenRefresh(account.getRefreshToken(), account.getUserId())
                .thenAccept(response -> {
                    JsonObject json = (JsonObject) JsonUtils.convertToJson(response);
                    String accessToken = json.get("access_token").getAsString();
                    account.setAccessToken(accessToken);
                    future.complete(new DataResult.Success<>(account));
                })
                .exceptionally(throwable -> {
                    future.complete(new DataResult.Error<>(throwable));
                    return null;
                });
        return future;
    }

    @NonNull
    public FutureDataResult<Account> logout() {
        return new FutureDataResult<>();
    }

    @NonNull
    public FutureDataResult<Account> delete() {
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

    public Account getAccount() {
        return account;
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
}
