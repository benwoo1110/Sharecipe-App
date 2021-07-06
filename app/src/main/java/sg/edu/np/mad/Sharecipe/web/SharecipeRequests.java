package sg.edu.np.mad.Sharecipe.web;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import java9.util.concurrent.CompletableFuture;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import sg.edu.np.mad.Sharecipe.models.User;
import sg.edu.np.mad.Sharecipe.utils.JsonUtils;

/**
 * Handles web requests to the server.
 */
public class SharecipeRequests {

    private static final MediaType JSON_TYPE = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType FILE_TYPE = MediaType.parse("application/octet-stream");
    private static final AsyncOkHttpClient CLIENT = new AsyncOkHttpClient();

    /**
     * GET `/hello` endpoint.
     *
     * @return Response from server.
     */
    @NonNull
    public static FutureWebResponse helloWorld() {
        return CLIENT.runAsync(new Request.Builder()
                .url(UrlPath.newBuilder()
                        .addPathSegment(UrlPath.HELLO)
                        .build())
                .get()
                .build());
    }

    /**
     * POST `/account/register` endpoint.
     *
     * @param username
     * @param password
     * @return Response from server.
     */
    @NonNull
    public static FutureWebResponse accountRegister(@NonNull String username,
                                                    @NonNull String password,
                                                    @Nullable String bio) {

        String payload;
        try {
            payload = new JSONObject()
                    .put("username", username)
                    .put("password", password)
                    .put("bio", bio)
                    .toString();
        } catch (JSONException e) {
            return FutureWebResponse.failedFuture(e);
        }

        return CLIENT.runAsync(new Request.Builder()
                .url(UrlPath.newBuilder()
                        .addPathSegment(UrlPath.ACCOUNT)
                        .addPathSegment(UrlPath.REGISTER)
                        .build())
                .post(RequestBody.create(payload, JSON_TYPE))
                .build());
    }

    /**
     * POST `/account/login` endpoint.
     *
     * @param username
     * @param password
     * @return Response from server.
     */
    @NonNull
    public static FutureWebResponse accountLogin(@NonNull String username,
                                                 @NonNull String password) {

        String payload;
        try {
            payload = new JSONObject()
                    .put("username", username)
                    .put("password", password)
                    .toString();
        } catch (JSONException e) {
            return FutureWebResponse.failedFuture(e);
        }

        return CLIENT.runAsync(new Request.Builder()
                .url(UrlPath.newBuilder()
                        .addPathSegment(UrlPath.ACCOUNT)
                        .addPathSegment(UrlPath.LOGIN)
                        .build())
                .post(RequestBody.create(payload, JSON_TYPE))
                .build());
    }

    /**
     * POST `/account/refresh` endpoint.
     *
     * @param refreshToken
     * @param userId
     * @return
     */
    @NonNull
    public static FutureWebResponse accountTokenRefresh(@NonNull String refreshToken,
                                                        int userId) {

        String payload;
        try {
            payload = new JSONObject()
                    .put("user_id", userId)
                    .toString();
        } catch (JSONException e) {
            return FutureWebResponse.failedFuture(e);
        }

        return CLIENT.runAsync(new Request.Builder()
                .url(UrlPath.newBuilder()
                        .addPathSegment(UrlPath.ACCOUNT)
                        .addPathSegment(UrlPath.REFRESH)
                        .build())
                .header("Authorization", "Bearer " + refreshToken)
                .post(RequestBody.create(payload, JSON_TYPE))
                .build());
    }

    /**
     * POST `/account/logout` endpoint.
     *
     * @param refreshToken
     * @param userId
     * @return
     */
    @NonNull
    public static FutureWebResponse accountLogout(@NonNull String refreshToken,
                                                  int userId) {

        String payload;
        try {
            payload = new JSONObject()
                    .put("user_id", userId)
                    .toString();
        } catch (JSONException e) {
            return FutureWebResponse.failedFuture(e);
        }

        return CLIENT.runAsync(new Request.Builder()
                .url(UrlPath.newBuilder()
                        .addPathSegment(UrlPath.ACCOUNT)
                        .addPathSegment(UrlPath.LOGOUT)
                        .build())
                .header("Authorization", "Bearer " + refreshToken)
                .post(RequestBody.create(payload, JSON_TYPE))
                .build());
    }

    /**
     * GET `/users` endpoint.
     *
     * @param accessToken
     * @param username
     * @return
     */
    @NonNull
    public static FutureWebResponse searchUsers(@NonNull String accessToken,
                                                @NonNull String username) {

        return CLIENT.runAsync(new Request.Builder()
                .url(UrlPath.newBuilder()
                        .addPathSegment(UrlPath.USERS)
                        .addQueryParameter("username", username)
                        .build())
                .header("Authorization", "Bearer " + accessToken)
                .get()
                .build());
    }

    /**
     * GET `/users/user_id` endpoint.
     *
     * @param accessToken
     * @param userId
     * @return Response from server.
     */
    @NonNull
    public static FutureWebResponse getUser(@NonNull String accessToken,
                                            int userId) {

        return CLIENT.runAsync(new Request.Builder()
                .url(UrlPath.newBuilder()
                        .addPathSegment(UrlPath.USERS)
                        .addPathSegment(String.valueOf(userId))
                        .build())
                .header("Authorization", "Bearer " + accessToken)
                .get()
                .build());
    }

    /**
     * PATCH `/users/user_id` endpoint.
     *
     * @param accessToken
     * @param userData
     * @return Response from server.
     */
    @NonNull
    public static FutureWebResponse editUser(@NonNull String accessToken,
                                             int userId ,
                                             JsonElement userData) {

        return CLIENT.runAsync(new Request.Builder()
                .url(UrlPath.newBuilder()
                        .addPathSegment(UrlPath.USERS)
                        .addPathSegment(String.valueOf(userId))
                        .build())
                .header("Authorization", "Bearer " + accessToken)
                .patch(RequestBody.create(userData.toString(), JSON_TYPE))
                .build());
    }

    /**
     * GET `/users/user_id/profileimage` endpoint.
     *
     * @param accessToken
     * @param userId
     * @return Response from server.
     */
    @NonNull
    public static FutureWebResponse getUserProfileImage(@NonNull String accessToken,
                                                        int userId) {

        return CLIENT.runAsync(new Request.Builder()
                .url(UrlPath.newBuilder()
                        .addPathSegment(UrlPath.USERS)
                        .addPathSegment(String.valueOf(userId))
                        .addPathSegment(UrlPath.PROFILE_IMAGE)
                        .build())
                .header("Authorization", "Bearer " + accessToken)
                .get()
                .build());
    }

    /**
     * PUT `/users/user_id/profileimage` endpoint.
     *
     * @param accessToken
     * @param userId
     * @return Response from server.
     */
    @NonNull
    public static FutureWebResponse setUserProfileImage(@NonNull String accessToken,
                                                        int userId,
                                                        File imageFile) {

        RequestBody image = RequestBody.create(imageFile, FILE_TYPE);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", imageFile.getName(), image)
                .build();

        return CLIENT.runAsync(new Request.Builder()
                .url(UrlPath.newBuilder()
                        .addPathSegment(UrlPath.USERS)
                        .addPathSegment(String.valueOf(userId))
                        .addPathSegment(UrlPath.PROFILE_IMAGE)
                        .build())
                .header("Authorization", "Bearer " + accessToken)
                .put(requestBody)
                .build());
    }

    /**
     * GET `/users/user_id/recipes/recipe_id` endpoint.
     *
     * @param accessToken
     * @param userId
     * @return
     */
    public static FutureWebResponse searchUserRecipes(@NonNull String accessToken,
                                                      int userId) {

        return CLIENT.runAsync(new Request.Builder()
                .url(UrlPath.newBuilder()
                        .addPathSegment(UrlPath.USERS)
                        .addPathSegment(String.valueOf(userId))
                        .addPathSegment(UrlPath.RECIPES)
                        .build())
                .header("Authorization", "Bearer " + accessToken)
                .get()
                .build());
    }

    /**
     * PUT `/users/user_id/recipes` endpoint.
     *
     * @param accessToken
     * @param userId
     * @param recipeData
     * @return
     */
    public static FutureWebResponse createRecipe(@NonNull String accessToken,
                                                 int userId,
                                                 JsonElement recipeData) {

        return CLIENT.runAsync(new Request.Builder()
                .url(UrlPath.newBuilder()
                        .addPathSegment(UrlPath.USERS)
                        .addPathSegment(String.valueOf(userId))
                        .addPathSegment(UrlPath.RECIPES)
                        .build())
                .header("Authorization", "Bearer " + accessToken)
                .put(RequestBody.create(recipeData.toString(), JSON_TYPE))
                .build());
    }

    /**
     * PATCH `/users/user_id/recipes/recipe_id` endpoint.
     *
     * @param accessToken
     * @param userId
     * @param recipeData
     * @return
     */
    public static FutureWebResponse editRecipe(@NonNull String accessToken,
                                               int userId,
                                               JsonElement recipeData) {

        return CLIENT.runAsync(new Request.Builder()
                .url(UrlPath.newBuilder()
                        .addPathSegment(UrlPath.USERS)
                        .addPathSegment(String.valueOf(userId))
                        .addPathSegment(UrlPath.RECIPES)
                        .build())
                .header("Authorization", "Bearer " + accessToken)
                .patch(RequestBody.create(recipeData.toString(), JSON_TYPE))
                .build());
    }

    /**
     * GET `/users/user_id/recipes/recipe_id` endpoint.
     *
     * @param accessToken
     * @param userId
     * @param recipeId
     * @return
     */
    public static FutureWebResponse getRecipe(@NonNull String accessToken,
                                              int userId,
                                              int recipeId) {

        return CLIENT.runAsync(new Request.Builder()
                .url(UrlPath.newBuilder()
                        .addPathSegment(UrlPath.USERS)
                        .addPathSegment(String.valueOf(userId))
                        .addPathSegment(UrlPath.RECIPES)
                        .addPathSegment(String.valueOf(recipeId))
                        .build())
                .header("Authorization", "Bearer " + accessToken)
                .get()
                .build());
    }

    /**
     * PUT `/users/user_id/recipes/recipe_id/images` endpoint.
     *
     * @param accessToken
     * @param userId
     * @param recipeId
     * @param imageFiles
     * @return
     */
    public static FutureWebResponse addRecipeImages(@NonNull String accessToken,
                                                              int userId,
                                                              int recipeId,
                                                              List<File> imageFiles) {

        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (File imageFile : imageFiles) {
            requestBodyBuilder.addFormDataPart(
                    "images",
                    imageFile.getName(),
                    RequestBody.create(imageFile, FILE_TYPE)
            );
        }

        return CLIENT.runAsync(new Request.Builder()
                .url(UrlPath.newBuilder()
                        .addPathSegment(UrlPath.USERS)
                        .addPathSegment(String.valueOf(userId))
                        .addPathSegment(UrlPath.RECIPES)
                        .addPathSegment(String.valueOf(recipeId))
                        .addPathSegment(UrlPath.IMAGES)
                        .build())
                .header("Authorization", "Bearer " + accessToken)
                .put(requestBodyBuilder.build())
                .build());
    }

    /**
     * GET `/users/user_id/recipes/recipe_id/images` endpoint.
     *
     * @param accessToken
     * @param userId
     * @param recipeId
     * @return
     */
    public static FutureWebResponse getRecipeImages(@NonNull String accessToken,
                                                              int userId,
                                                              int recipeId) {

        return CLIENT.runAsync(new Request.Builder()
                .url(UrlPath.newBuilder()
                        .addPathSegment(UrlPath.USERS)
                        .addPathSegment(String.valueOf(userId))
                        .addPathSegment(UrlPath.RECIPES)
                        .addPathSegment(String.valueOf(recipeId))
                        .addPathSegment(UrlPath.IMAGES)
                        .build())
                .header("Authorization", "Bearer " + accessToken)
                .get()
                .build());
    }
}
