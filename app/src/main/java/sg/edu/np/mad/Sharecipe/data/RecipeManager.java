package sg.edu.np.mad.Sharecipe.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.gson.JsonElement;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import okhttp3.ResponseBody;
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
    public FutureDataResult<Recipe> create(Recipe newRecipe) {
        FutureDataResult<Recipe> future = new FutureDataResult<>();

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

    /**
     * Updates existing recipe with changed information.
     *
     * @param recipe    Recipe data to update.
     * @return Future result of updated recipe data.
     */
    public FutureDataResult<Recipe> update(Recipe recipe) {
        FutureDataResult<Recipe> future = new FutureDataResult<>();

        accountManager.getOrRefreshAccount().onSuccess(account -> {
            JsonElement recipeData = JsonUtils.convertToJson(recipe);
            SharecipeRequests.createRecipe(account.getAccessToken(), account.getUserId(), recipeData).thenAccept(response -> {
                JsonElement json = JsonUtils.convertToJson(response);
                if (!response.isSuccessful()) {
                    JsonElement message = json.getAsJsonObject().get("message");
                    future.complete(new DataResult.Failed<>(message != null ? message.getAsString() : "An unknown error occurred!"));
                    return;
                }
                Recipe updatedRecipe = JsonUtils.convertToObject(json, Recipe.class);
                if (updatedRecipe == null) {
                    future.complete(new DataResult.Failed<>("Received invalid data."));
                    return;
                }
                future.complete(new DataResult.Success<>(updatedRecipe));
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
     * Gets a recipe data.
     *
     * @param userId    Author of the recipe
     * @param recipeId  Target recipe to get info on.
     * @return Future result of the recipe data.
     */
    public FutureDataResult<Recipe> get(int userId, int recipeId) {
        FutureDataResult<Recipe> future = new FutureDataResult<>();

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

    public FutureDataResult<Void> addImages(Recipe recipe, List<File> imageFiles) {
        FutureDataResult<Void> future = new FutureDataResult<>();

        accountManager.getOrRefreshAccount().onSuccess(account -> {
            SharecipeRequests.addRecipeImages(account.getAccessToken(), recipe.getUserId(), recipe.getRecipeId(), imageFiles).thenAccept(response -> {
                if (!response.isSuccessful()) {
                    JsonElement json = JsonUtils.convertToJson(response);
                    JsonElement message = json.getAsJsonObject().get("message");
                    future.complete(new DataResult.Failed<>(message != null ? message.getAsString() : "An unknown error occurred!"));
                    return;
                }
                future.complete(new DataResult.Success<>(null));
            });
        })
        .onFailed(reason -> future.complete(new DataResult.Failed<>(reason)))
        .onError(throwable -> future.complete(new DataResult.Error<>(throwable)));

        return future;
    }

    public FutureDataResult<List<Bitmap>> getImages(Recipe recipe) {
        FutureDataResult<List<Bitmap>> future = new FutureDataResult<>();

        accountManager.getOrRefreshAccount().onSuccess(account -> {
            SharecipeRequests.getRecipeImages(account.getAccessToken(), recipe.getUserId(), recipe.getRecipeId()).thenAccept(response -> {
                if (!response.isSuccessful()) {
                    JsonElement json = JsonUtils.convertToJson(response);
                    JsonElement message = json.getAsJsonObject().get("message");
                    future.complete(new DataResult.Failed<>(message != null ? message.getAsString() : "An unknown error occurred!"));
                    return;
                }
                ResponseBody body = response.body();
                if (body == null) {
                    future.complete(new DataResult.Failed<>("No profile image data"));
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
            });
        })
        .onFailed(reason -> future.complete(new DataResult.Failed<>(reason)))
        .onError(throwable -> future.complete(new DataResult.Error<>(throwable)));

        return future;
    }
}
