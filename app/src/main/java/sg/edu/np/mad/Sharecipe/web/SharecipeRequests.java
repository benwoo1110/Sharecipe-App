package sg.edu.np.mad.Sharecipe.web;

import androidx.annotation.NonNull;

import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import java9.util.concurrent.CompletableFuture;
import java9.util.function.Function;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Handles web requests to the server.
 */
public class SharecipeRequests {

    private static final MediaType JSON_TYPE = MediaType.parse("application/json; charset=utf-8");
    private static final AsyncOkHttpClient CLIENT = new AsyncOkHttpClient();

    public static final Function<Response, JSONObject> DECODE_TO_JSON = (response) -> {
        try {
            return new JSONObject(response.body().string());
        } catch (IOException | JSONException e) {
            return null;
        }
    };

    /**
     * `/hello` endpoint.
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
     * `/account/register` endpoint.
     *
     * @param username
     * @param password
     * @return Response from server.
     */
    @NonNull
    public static CompletableFuture<Response> accountRegister(@NonNull String username, @NonNull String password) {
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
                        .addPathSegment(UrlPath.REGISTER)
                        .build())
                .post(RequestBody.create(payload, JSON_TYPE))
                .build());
    }

    /**
     * `/account/login` endpoint.
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
     * `/users` endpoint.
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
     * `/users/user_id` endpoint.
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
     * `/users/user_id/recipes` endpoint.
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
}
