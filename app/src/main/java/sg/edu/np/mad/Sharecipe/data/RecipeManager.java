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
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import okhttp3.ResponseBody;
import sg.edu.np.mad.Sharecipe.models.PartialRecipe;
import sg.edu.np.mad.Sharecipe.models.Recipe;
import sg.edu.np.mad.Sharecipe.models.RecipeImage;
import sg.edu.np.mad.Sharecipe.models.RecipeLike;
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
            instance = new RecipeManager(AccountManager.getInstance(context.getApplicationContext()), BitmapCacheManager.getInstance());
        }
        return instance;
    }

    private final AccountManager accountManager;
    private final BitmapCacheManager bitmapCacheManager;
    private final Cache<Integer, Recipe> recipeCache = CacheBuilder.newBuilder()
            .expireAfterAccess(10, TimeUnit.MINUTES)
            .maximumSize(500)
            .build();

    public RecipeManager(AccountManager accountManager, BitmapCacheManager bitmapCacheManager) {
        this.accountManager = accountManager;
        this.bitmapCacheManager = bitmapCacheManager;
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
                recipeCache.put(recipe.getRecipeId(), recipe);
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
            SharecipeRequests.patchRecipe(account.getAccessToken(), modifiedRecipe.getRecipeId(), recipeData).onSuccessModel(future, Recipe.class, (response, recipe) -> {
                recipeCache.put(recipe.getRecipeId(), recipe);
                future.complete(new DataResult.Success<>(recipe));
            }).onFailed(future).onError(future);
        }).onFailed(future).onError(future);

        return future;
    }

    /**
     *
     *
     * @param recipe
     * @param imageFiles
     * @return
     */
    public FutureDataResult<Void> addImages(PartialRecipe recipe, List<File> imageFiles) {
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
                recipeCache.put(recipe.getRecipeId(), recipe);
                future.complete(new DataResult.Success<>(recipe));
            }).onFailed(future).onError(future);
        }).onFailed(future).onError(future);

        return future;
    }

    /**
     *
     *
     * @param recipe
     * @return
     */
    public FutureDataResult<Bitmap> getIcon(PartialRecipe recipe) {
        RecipeImage icon = recipe.getIcon();
        if (icon == null || Strings.isNullOrEmpty(icon.getFileId())) {
            return FutureDataResult.completed(new DataResult.Failed<>("Recipe does not have icon image."));
        }

        Bitmap cacheImage = bitmapCacheManager.getBitmapFromMemCache(icon.getFileId());
        if (cacheImage != null) {
            return FutureDataResult.completed(cacheImage);
        }

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
                bitmapCacheManager.addBitmapToMemoryCache(icon.getFileId(), bitmap);
                future.complete(new DataResult.Success<>(bitmap));
            }).onFailed(future).onError(future);
        }).onFailed(future).onError(future);

        return future;
    }

    /**
     *
     *
     * @param recipe
     * @return
     */
    public FutureDataResult<List<Bitmap>> getImages(PartialRecipe recipe) {
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
                    ZipEntry entry;
                    while ((entry = imageZipStream.getNextEntry()) != null) {
                        Bitmap bitmap = BitmapFactory.decodeStream(imageZipStream);
                        if (bitmap != null) {
                            images.add(bitmap);
                            bitmapCacheManager.addBitmapToMemoryCache(entry.getName(), bitmap);
                        }
                    }
                } catch (IOException e) {
                    future.complete(new DataResult.Error<>(e));
                }
                future.complete(new DataResult.Success<>(images));
            }).onFailed(future).onError(future);
        }).onFailed(future).onError(future);

        return future;
    }

    /**
     *
     *
     * @param recipe
     * @return
     */
    public FutureDataResult<List<RecipeLike>> getLikes(PartialRecipe recipe) {
        FutureDataResult<List<RecipeLike>> future = new FutureDataResult<>();

        accountManager.getOrRefreshAccount().onSuccess(account -> {
            SharecipeRequests.getRecipeLikes(account.getAccessToken(), recipe.getRecipeId()).onSuccessJson(future, (response, json) -> {
                List<RecipeLike> recipes = new ArrayList<>();
                for (JsonElement likeData : json.getAsJsonArray()) {
                    recipes.add(JsonUtils.convertToObject(likeData, RecipeLike.class));
                }
                future.complete(new DataResult.Success<>(recipes));
            }).onFailed(future).onError(future);
        }).onFailed(future).onError(future);

        return future;
    }

    /**
     *
     *
     * @param userId
     * @return
     */
    public FutureDataResult<List<PartialRecipe>> getAllForUser(int userId) {
        FutureDataResult<List<PartialRecipe>> future = new FutureDataResult<>();

        accountManager.getOrRefreshAccount().onSuccess(account -> {
            SharecipeRequests.getUserRecipes(account.getAccessToken(), userId).onSuccessJson(future, (response, json) -> {
                List<PartialRecipe> recipes = new ArrayList<>();
                for (JsonElement recipeData : json.getAsJsonArray()) {
                    recipes.add(JsonUtils.convertToObject(recipeData, PartialRecipe.class));
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
    public FutureDataResult<List<PartialRecipe>> getAccountRecipes() {
        if (!accountManager.isLoggedIn()) {
            return FutureDataResult.completed(new DataResult.Failed<>("No account logged in!"));
        }
        return getAllForUser(accountManager.getAccount().getUserId());
    }

    /**
     *
     *
     * @param recipe
     * @return
     */
    public FutureDataResult<Void> accountLikeRecipe(PartialRecipe recipe) {
        FutureDataResult<Void> future = new FutureDataResult<>();

        accountManager.getOrRefreshAccount().onSuccess(account -> {
            SharecipeRequests.putRecipeLikes(account.getAccessToken(), recipe.getRecipeId()).onSuccess(response -> {
                future.complete(new DataResult.Success<>(null));
            }).onFailed(future).onError(future);
        }).onFailed(future).onError(future);

        return future;
    }

    /**
     *
     *
     * @param recipe
     * @return
     */
    public FutureDataResult<Void> accountUnlikeRecipe(PartialRecipe recipe) {
        FutureDataResult<Void> future = new FutureDataResult<>();

        accountManager.getOrRefreshAccount().onSuccess(account -> {
            SharecipeRequests.deleteRecipeLikes(account.getAccessToken(), recipe.getRecipeId()).onSuccess(response -> {
                future.complete(new DataResult.Success<>(null));
            }).onFailed(future).onError(future);
        }).onFailed(future).onError(future);

        return future;
    }
}
