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
import java.util.zip.ZipInputStream;

import okhttp3.ResponseBody;
import sg.edu.np.mad.Sharecipe.models.Recipe;
import sg.edu.np.mad.Sharecipe.utils.DataResult;
import sg.edu.np.mad.Sharecipe.utils.FutureDataResult;
import sg.edu.np.mad.Sharecipe.utils.JsonUtils;
import sg.edu.np.mad.Sharecipe.web.SharecipeRequests;

public class RecipeManager {

    private static RecipeManager instance;

    /**
     * Gets common {@link RecipeManager} instance throughout the app.
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
    public FutureDataResult<Recipe> create(Recipe newRecipe) {
        FutureDataResult<Recipe> future = new FutureDataResult<>();

        accountManager.getOrRefreshAccount().onSuccess(account -> {
            JsonElement recipeData = JsonUtils.convertToJson(newRecipe);
            SharecipeRequests.putRecipes(account.getAccessToken(), recipeData).onSuccessModel(future, Recipe.class, (response, recipe) -> {
                //TODO: Add to cache
                future.complete(new DataResult.Success<>(recipe));
            }).onFailed(future).onError(future);
        }).onFailed(future).onError(future);

        return future;
    }

    /**
     * Updates existing recipe with changed information.
     *
     * @param modifiedRecipe    Recipe data to update.
     * @return Future result of updated recipe data.
     */
    public FutureDataResult<Recipe> update(Recipe modifiedRecipe) {
        FutureDataResult<Recipe> future = new FutureDataResult<>();

        accountManager.getOrRefreshAccount().onSuccess(account -> {
            JsonElement recipeData = JsonUtils.convertToJson(modifiedRecipe);
            SharecipeRequests.putRecipes(account.getAccessToken(), recipeData).onSuccessModel(future, Recipe.class, (response, recipe) -> {
                future.complete(new DataResult.Success<>(recipe));
            }).onFailed(future).onError(future);
        }).onFailed(future).onError(future);

        return future;
    }

    /**
     * Gets a recipe data.
     *
     * @param recipeId  Target recipe to get info on.
     * @return Future result of the recipe data.
     */
    public FutureDataResult<Recipe> get(int recipeId) {

        FutureDataResult<Recipe> future = new FutureDataResult<>();

        accountManager.getOrRefreshAccount().onSuccess(account -> {
            SharecipeRequests.getRecipe(account.getAccessToken(), recipeId).onSuccessModel(future, Recipe.class, (response, recipe) -> {
                future.complete(new DataResult.Success<>(recipe));
            }).onFailed(future).onError(future);
        }).onFailed(future).onError(future);

        return future;
    }

    public FutureDataResult<Bitmap> getIcon(Recipe recipe) {
        FutureDataResult<Bitmap> future = new FutureDataResult<>();
        accountManager.getOrRefreshAccount().onSuccess(account -> {
            SharecipeRequests.getRecipeIcon(account.getAccessToken(), recipe.getRecipeId()).onSuccess(response -> {
                ResponseBody body = response.body();
                if (body == null) {
                    future.complete(new DataResult.Failed<>("No icon image data."));
                    return;
                }
                Bitmap bitmap = BitmapFactory.decodeStream(body.byteStream());
                if (bitmap == null) {
                    future.complete(new DataResult.Failed<>("Failed to load data into image."));
                    return;
                }
                future.complete(new DataResult.Success<>(bitmap));
            }).onFailed(future).onError(future);
        }).onFailed(future).onError(future);

        return future;
    }

    public FutureDataResult<Void> addImages(Recipe recipe, List<File> imageFiles) {
        if (imageFiles == null || imageFiles.isEmpty()) {
            return FutureDataResult.completed(new DataResult.Failed<>("No recipe images to upload"));
        }

        FutureDataResult<Void> future = new FutureDataResult<>();

        accountManager.getOrRefreshAccount().onSuccess(account -> {
            SharecipeRequests.putRecipeImages(account.getAccessToken(), recipe.getRecipeId(), imageFiles).onSuccess(response -> {
                future.complete(new DataResult.Success<>(null));
            }).onFailed(future).onError(future);
        }).onFailed(future).onError(future);

        return future;
    }

    public FutureDataResult<List<Bitmap>> getImages(Recipe recipe) {
        FutureDataResult<List<Bitmap>> future = new FutureDataResult<>();

        accountManager.getOrRefreshAccount().onSuccess(account -> {
            SharecipeRequests.getRecipeImages(account.getAccessToken(), recipe.getRecipeId()).onSuccess(response -> {
                ResponseBody body = response.body();
                if (body == null) {
                    future.complete(new DataResult.Failed<>("No images data."));
                    return;
                }
                List<Bitmap> images = new ArrayList<>();
                try (ZipInputStream imageZipStream = new ZipInputStream(body.byteStream())) {
                    while (imageZipStream.getNextEntry() != null) {
                        images.add(BitmapFactory.decodeStream(imageZipStream));
                    }
                } catch (IOException e) {
                    future.complete(new DataResult.Error<>(e));
                }
                future.complete(new DataResult.Success<>(images));
            }).onFailed(future).onError(future);
        }).onFailed(future).onError(future);

        return future;
    }

    public FutureDataResult<List<Recipe>> getAllForUser(int userId) {
        FutureDataResult<List<Recipe>> future = new FutureDataResult<>();

        accountManager.getOrRefreshAccount().onSuccess(account -> {
            SharecipeRequests.getUserRecipes(account.getAccessToken(), userId).onSuccessJson(future, (response, json) -> {
                List<Recipe> recipes = new ArrayList<>();
                for (JsonElement recipeData : json.getAsJsonArray()) {
                    recipes.add(JsonUtils.convertToObject(recipeData, Recipe.class));
                }
                future.complete(new DataResult.Success<>(recipes));
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
    public FutureDataResult<List<Recipe>> getAccountRecipe() {
        if (!accountManager.isLoggedIn()) {
            return FutureDataResult.completed(new DataResult.Failed<>("No account logged in!"));
        }
        return getAllForUser(accountManager.getAccount().getUserId());
    }
}
