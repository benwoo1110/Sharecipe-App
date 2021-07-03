package sg.edu.np.mad.Sharecipe.data;

import android.content.Context;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.gson.JsonElement;

import java.util.concurrent.TimeUnit;

import sg.edu.np.mad.Sharecipe.models.Recipe;
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

    /**
     * Saves a new recipe. This will never replace an existing one.
     *
     * @param newRecipe New recipe data to be saved.
     * @return Future result of newly saved recipe data.
     */
    public FutureDataResult<Recipe> save(Recipe newRecipe) {
        FutureDataResult<Recipe> future = new FutureDataResult<>();
        if (!accountManager.isLoggedIn()) {
            future.complete(new DataResult.Failed<>("No account logged in!"));
            return future;
        }
        accountManager.getOrRefreshAccount().onSuccess(account -> {
            JsonElement recipeData = JsonUtils.convertToJson(newRecipe);
            SharecipeRequests.createRecipe(account.getAccessToken(), account.getUserId(), recipeData).thenAccept(response -> {
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
        })
        .onFailed(reason -> future.complete(new DataResult.Failed<>(reason)))
        .onError(throwable -> future.complete(new DataResult.Error<>(throwable)));

        return future;
    }

    //TODO: update recipe

    /**
     * Gets a recipe data.
     *
     * @param userId    Author of the recipe
     * @param recipeId  Target recipe to get info on.
     * @return Future result of the recipe data.
     */
    public FutureDataResult<Recipe> get(int userId, int recipeId) {
        FutureDataResult<Recipe> future = new FutureDataResult<>();
        if (!accountManager.isLoggedIn()) {
            future.complete(new DataResult.Failed<>("No account logged in!"));
            return future;
        }

        accountManager.getOrRefreshAccount().onSuccess(account -> {
            SharecipeRequests.getRecipe(account.getAccessToken(), userId, recipeId).thenAccept(response -> {
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
        })
        .onFailed(reason -> future.complete(new DataResult.Failed<>(reason)))
        .onError(throwable -> future.complete(new DataResult.Error<>(throwable)));

        return future;
    }
}
