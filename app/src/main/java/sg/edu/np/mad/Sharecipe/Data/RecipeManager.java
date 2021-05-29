package sg.edu.np.mad.Sharecipe.Data;

import android.content.Context;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.gson.JsonElement;

import java.util.List;
import java.util.concurrent.TimeUnit;

import sg.edu.np.mad.Sharecipe.Models.Account;
import sg.edu.np.mad.Sharecipe.Models.Recipe;
import sg.edu.np.mad.Sharecipe.Models.User;
import sg.edu.np.mad.Sharecipe.utils.DataResult;
import sg.edu.np.mad.Sharecipe.utils.FutureDataResult;
import sg.edu.np.mad.Sharecipe.utils.JsonUtils;
import sg.edu.np.mad.Sharecipe.web.SharecipeRequests;

public class RecipeManager {

    private static RecipeManager instance;

    /**
     * Gets common {@link UserManager} instance throughout the app.
     *
     * @param context   Application context for share preference loading.
     * @return The instance.
     */
    public static RecipeManager getInstance(Context context) {
        if (instance == null) {
            instance = new RecipeManager(AccountManager.getInstance(context.getApplicationContext()));
        }
        return instance;
    }

    private final AccountManager accountManager;
    private final Cache<Integer, Recipe> recipeCache = CacheBuilder.newBuilder()
            .expireAfterAccess(10, TimeUnit.MINUTES)
            .build();

    public RecipeManager(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    public FutureDataResult<Recipe> saveNewRecipe(Recipe newRecipe) {
        FutureDataResult<Recipe> future = new FutureDataResult<>();
        if (!accountManager.isLoggedIn()) {
            future.complete(new DataResult.Failed<>("No account logged in!"));
            return future;
        }

        Account account = accountManager.getAccount();
        JsonElement recipeData = JsonUtils.convertToJson(newRecipe);

        SharecipeRequests.createRecipe(account.getAccessToken(), account.getUserId(), recipeData)
                .thenAccept(response -> {
                    JsonElement json = JsonUtils.convertToJson(response);
                    if (!response.isSuccessful()) {
                        JsonElement message = json.getAsJsonObject().get("message");
                        future.complete(new DataResult.Failed<>(message != null ? message.getAsString() : "An unknown error occurred!"));
                        return;
                    }
                    Recipe recipe = JsonUtils.convertToObject(json, Recipe.class);
                    if (recipe == null) {
                        future.complete(new DataResult.Failed<>("Received invalid data."));
                        return;
                    }
                    future.complete(new DataResult.Success<>(recipe));
                })
                .exceptionally(throwable -> {
                    future.complete(new DataResult.Error<>(throwable));
                    return null;
                });

        return future;
    }
}
