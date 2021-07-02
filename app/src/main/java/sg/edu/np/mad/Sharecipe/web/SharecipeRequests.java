package sg.edu.np.mad.Sharecipe.web;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import java9.util.concurrent.CompletableFuture;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
    public static CompletableFuture<Response> helloWorld() {
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
    public static CompletableFuture<Response> accountRegister(@NonNull String username, @NonNull String password, @Nullable String bio) {
        String payload;
        try {
            payload = new JSONObject()
                    .put("username", username)
                    .put("password", password)
                    .put("bio", bio)
                    .toString();
        } catch (JSONException e) {
            return CompletableFuture.failedFuture(e);
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
    public static CompletableFuture<Response> accountLogin(@NonNull String username, @NonNull String password) {
        String payload;
        try {
            payload = new JSONObject()
                    .put("username", username)
                    .put("password", password)
                    .toString();
        } catch (JSONException e) {
            return CompletableFuture.failedFuture(e);
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
    public static CompletableFuture<Response> accountTokenRefresh(@NonNull String refreshToken, int userId) {
        String payload;
        try {
            payload = new JSONObject()
                    .put("user_id", userId)
                    .toString();
        } catch (JSONException e) {
            return CompletableFuture.failedFuture(e);
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
    public static CompletableFuture<Response> accountLogout(@NonNull String refreshToken, int userId) {
        String payload;
        try {
            payload = new JSONObject()
                    .put("user_id", userId)
                    .toString();
        } catch (JSONException e) {
            return CompletableFuture.failedFuture(e);
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
    public static CompletableFuture<Response> searchUsers(@NonNull String accessToken, @NonNull String username) {
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
    public static CompletableFuture<Response> getUser(@NonNull String accessToken, int userId) {
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
     * GET `/users/user_id/profileimage` endpoint.
     *
     * @param accessToken
     * @param userId
     * @return Response from server.
     */
    @NonNull
    public static CompletableFuture<Response> getUserProfileImage(@NonNull String accessToken, int userId) {
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
     * GET `/users/user_id/profileimage` endpoint.
     *
     * @param accessToken
     * @param userId
     * @return Response from server.
     */
    @NonNull
    public static CompletableFuture<Response> setUserProfileImage(@NonNull String accessToken, int userId, File imageFile) {
        RequestBody image = RequestBody.create(imageFile, FILE_TYPE);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("text", "text")
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
     * PUT `/users/user_id/recipes` endpoint.
     *
     * @param accessToken
     * @param userId
     * @param recipeData
     * @return
     */
    public static CompletableFuture<Response> createRecipe(@NonNull String accessToken, int userId, JsonElement recipeData) {
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
     * PUT `/users/user_id/recipes/recipe_id` endpoint.
     *
     * @param accessToken
     * @param userId
     * @param recipeId
     * @return
     */
    public static CompletableFuture<Response> getRecipe(@NonNull String accessToken, int userId, int recipeId) {
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
}
