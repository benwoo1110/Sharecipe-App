package sg.edu.np.mad.Sharecipe.web;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.JsonElement;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

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
    public static FutureWebResponse getHello() {
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
    public static FutureWebResponse postAccountRegister(@NonNull String username,
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
    public static FutureWebResponse postAccountLogin(@NonNull String username,
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
    public static FutureWebResponse postAccountRefresh(@NonNull String refreshToken,
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
    public static FutureWebResponse postAccountLogout(@NonNull String refreshToken,
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
    public static FutureWebResponse getUsers(@NonNull String accessToken,
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
    public static FutureWebResponse patchUser(@NonNull String accessToken,
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
    public static FutureWebResponse putUserProfileImage(@NonNull String accessToken,
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
     * GET `/users/user_id/follows` endpoint.
     *
     * @param accessToken
     * @param userId
     * @return Response from server.
     */
    @NotNull
    public static FutureWebResponse getUserFollows(@NonNull String accessToken,
                                                   int userId) {

        return CLIENT.runAsync(new Request.Builder()
                .url(UrlPath.newBuilder()
                        .addPathSegment(UrlPath.USERS)
                        .addPathSegment(String.valueOf(userId))
                        .addPathSegment(UrlPath.FOLLOWS)
                        .build())
                .header("Authorization", "Bearer " + accessToken)
                .get()
                .build());
    }

    /**
     * GET `/users/user_id/followers` endpoint.
     *
     * @param accessToken
     * @param userId
     * @return Response from server.
     */
    @NotNull
    public static FutureWebResponse getUserFollowers(@NonNull String accessToken,
                                                     int userId) {

        return CLIENT.runAsync(new Request.Builder()
                .url(UrlPath.newBuilder()
                        .addPathSegment(UrlPath.USERS)
                        .addPathSegment(String.valueOf(userId))
                        .addPathSegment(UrlPath.FOLLOWERS)
                        .build())
                .header("Authorization", "Bearer " + accessToken)
                .get()
                .build());
    }

    /**
     * PUT `/users/user_id/follows` endpoint.
     *
     * @param accessToken
     * @param userId
     * @return Response from server.
     */
    @NotNull
    public static FutureWebResponse putUserFollows(@NonNull String accessToken,
                                                   int userId,
                                                   int followId) {

        String payload;
        try {
            payload = new JSONObject()
                    .put("follow_id", followId)
                    .toString();
        } catch (JSONException e) {
            return FutureWebResponse.failedFuture(e);
        }

        return CLIENT.runAsync(new Request.Builder()
                .url(UrlPath.newBuilder()
                        .addPathSegment(UrlPath.USERS)
                        .addPathSegment(String.valueOf(userId))
                        .addPathSegment(UrlPath.FOLLOWS)
                        .build())
                .header("Authorization", "Bearer " + accessToken)
                .put(RequestBody.create(payload, JSON_TYPE))
                .build());
    }

    /**
     * DELETE `/users/user_id/follows` endpoint.
     *
     * @param accessToken
     * @param userId
     * @return Response from server.
     */
    @NotNull
    public static FutureWebResponse deleteUserFollows(@NonNull String accessToken,
                                                      int userId,
                                                      int followId) {

        String payload;
        try {
            payload = new JSONObject()
                    .put("follow_id", followId)
                    .toString();
        } catch (JSONException e) {
            return FutureWebResponse.failedFuture(e);
        }

        return CLIENT.runAsync(new Request.Builder()
                .url(UrlPath.newBuilder()
                        .addPathSegment(UrlPath.USERS)
                        .addPathSegment(String.valueOf(userId))
                        .addPathSegment(UrlPath.FOLLOWS)
                        .build())
                .header("Authorization", "Bearer " + accessToken)
                .delete(RequestBody.create(payload, JSON_TYPE))
                .build());
    }

    /**
     * GET `/users/user_id/recipes` endpoint.
     *
     * @param accessToken
     * @param userId
     * @return
     */
    public static FutureWebResponse getUserRecipes(@NonNull String accessToken,
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
     * GET `/users/user_id/recipes/likes` endpoint.
     *
     * @param accessToken
     * @param userId
     * @return
     */
    public static FutureWebResponse getUserRecipeLikes(@NonNull String accessToken,
                                                       int userId) {

        return CLIENT.runAsync(new Request.Builder()
                .url(UrlPath.newBuilder()
                        .addPathSegment(UrlPath.USERS)
                        .addPathSegment(String.valueOf(userId))
                        .addPathSegment(UrlPath.RECIPES)
                        .addPathSegment(UrlPath.LIKES)
                        .build())
                .header("Authorization", "Bearer " + accessToken)
                .get()
                .build());
    }

    /**
     * GET `/recipes` endpoint.
     *
     * @param accessToken
     * @return
     */
    public static FutureWebResponse getRecipes(@NonNull String accessToken) {

        return CLIENT.runAsync(new Request.Builder()
                .url(UrlPath.newBuilder()
                        .addPathSegment(UrlPath.RECIPES)
                        .build())
                .header("Authorization", "Bearer " + accessToken)
                .get()
                .build());
    }

    /**
     * PUT `/recipes` endpoint.
     *
     * @param accessToken
     * @param recipeData
     * @return
     */
    public static FutureWebResponse putRecipes(@NonNull String accessToken,
                                               JsonElement recipeData) {

        return CLIENT.runAsync(new Request.Builder()
                .url(UrlPath.newBuilder()
                        .addPathSegment(UrlPath.RECIPES)
                        .build())
                .header("Authorization", "Bearer " + accessToken)
                .put(RequestBody.create(recipeData.toString(), JSON_TYPE))
                .build());
    }

    /**
     * GET `/recipes/recipe_id` endpoint.
     *
     * @param accessToken
     * @param recipeId
     * @return
     */
    public static FutureWebResponse getRecipe(@NonNull String accessToken,
                                              int recipeId) {

        return CLIENT.runAsync(new Request.Builder()
                .url(UrlPath.newBuilder()
                        .addPathSegment(UrlPath.RECIPES)
                        .addPathSegment(String.valueOf(recipeId))
                        .build())
                .header("Authorization", "Bearer " + accessToken)
                .get()
                .build());
    }

    /**
     * PATCH `/recipes/recipe_id` endpoint.
     *
     * @param accessToken
     * @param recipeId
     * @param recipeData
     * @return
     */
    public static FutureWebResponse patchRecipe(@NonNull String accessToken,
                                                int recipeId,
                                                JsonElement recipeData) {

        return CLIENT.runAsync(new Request.Builder()
                .url(UrlPath.newBuilder()
                        .addPathSegment(UrlPath.RECIPES)
                        .addPathSegment(String.valueOf(recipeId))
                        .build())
                .header("Authorization", "Bearer " + accessToken)
                .patch(RequestBody.create(recipeData.toString(), JSON_TYPE))
                .build());
    }

    /**
     * GET `/recipes/recipe_id/icon` endpoint.
     *
     * @param accessToken
     * @param recipeId
     * @return
     */
    public static FutureWebResponse getRecipeIcon(@NonNull String accessToken,
                                                  int recipeId) {

        return CLIENT.runAsync(new Request.Builder()
                .url(UrlPath.newBuilder()
                        .addPathSegment(UrlPath.RECIPES)
                        .addPathSegment(String.valueOf(recipeId))
                        .addPathSegment(UrlPath.ICON)
                        .build())
                .header("Authorization", "Bearer " + accessToken)
                .get()
                .build());
    }

    /**
     * PUT `/recipes/recipe_id/images` endpoint.
     *
     * @param accessToken
     * @param recipeId
     * @param imageFiles
     * @return
     */
    public static FutureWebResponse putRecipeImages(@NonNull String accessToken,
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
     * @param recipeId
     * @return
     */
    public static FutureWebResponse getRecipeImages(@NonNull String accessToken,
                                                    int recipeId) {

        return CLIENT.runAsync(new Request.Builder()
                .url(UrlPath.newBuilder()
                        .addPathSegment(UrlPath.RECIPES)
                        .addPathSegment(String.valueOf(recipeId))
                        .addPathSegment(UrlPath.IMAGES)
                        .build())
                .header("Authorization", "Bearer " + accessToken)
                .get()
                .build());
    }

    /**
     *  GET `/users/user_id/recipes/recipe_id/likes` endpoint.
     *
     * @param accessToken
     * @param recipeId
     * @return
     */
    public static FutureWebResponse getRecipeLikes(@NonNull String accessToken,
                                                   int recipeId) {

        return CLIENT.runAsync(new Request.Builder()
                .url(UrlPath.newBuilder()
                        .addPathSegment(UrlPath.RECIPES)
                        .addPathSegment(String.valueOf(recipeId))
                        .addPathSegment(UrlPath.LIKES)
                        .build())
                .header("Authorization", "Bearer " + accessToken)
                .get()
                .build());
    }

    /**
     *  PUT `/users/user_id/recipes/recipe_id/likes` endpoint.
     *
     * @param accessToken
     * @param recipeId
     * @return
     */
    public static FutureWebResponse putRecipeLikes(@NonNull String accessToken,
                                                   int recipeId) {

        return CLIENT.runAsync(new Request.Builder()
                .url(UrlPath.newBuilder()
                        .addPathSegment(UrlPath.RECIPES)
                        .addPathSegment(String.valueOf(recipeId))
                        .addPathSegment(UrlPath.LIKES)
                        .build())
                .header("Authorization", "Bearer " + accessToken)
                .put(RequestBody.create("", null))
                .build());
    }

    /**
     *  DELETE `/users/user_id/recipes/recipe_id/likes` endpoint.
     *
     * @param accessToken
     * @param recipeId
     * @return
     */
    public static FutureWebResponse deleteRecipeLikes(@NonNull String accessToken,
                                                      int recipeId) {

        return CLIENT.runAsync(new Request.Builder()
                .url(UrlPath.newBuilder()
                        .addPathSegment(UrlPath.RECIPES)
                        .addPathSegment(String.valueOf(recipeId))
                        .addPathSegment(UrlPath.LIKES)
                        .build())
                .header("Authorization", "Bearer " + accessToken)
                .delete(RequestBody.create("", null))
                .build());
    }

    /**
     * GET `/search` endpoint.
     *
     * @param accessToken
     * @param queryString
     * @return
     */
    public static FutureWebResponse getSearch(@NonNull String accessToken,
                                              @Nullable String queryString) {

        return CLIENT.runAsync(new Request.Builder()
                .url(UrlPath.newBuilder()
                        .addPathSegment(UrlPath.SEARCH)
                        .addQueryParameter("search_string", queryString)
                        .build())
                .header("Authorization", "Bearer " + accessToken)
                .get()
                .build());
    }

    /**
     * GET `/discover` endpoint.
     *
     * @param accessToken
     * @return
     */
    public static FutureWebResponse getDiscover(@NonNull String accessToken) {

        return CLIENT.runAsync(new Request.Builder()
                .url(UrlPath.newBuilder()
                        .addPathSegment(UrlPath.DISCOVER)
                        .build())
                .header("Authorization", "Bearer " + accessToken)
                .get()
                .build());
    }
}
