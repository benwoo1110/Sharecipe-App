package sg.edu.np.mad.Sharecipe.web;

import androidx.annotation.NonNull;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import java9.util.concurrent.CompletableFuture;
import java9.util.function.Function;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Handles web requests to the server.
 */
public class SharecipeRequests {

    private static final MediaType JSON_TYPE = MediaType.parse("application/json; charset=utf-8");
    private static final AsyncOkHttpClient client = new AsyncOkHttpClient();
    private static final Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

    public static final Function<Response, JSONObject> DECODE_TO_JSON = (response) -> {
        try {
            return new JSONObject(response.body().string());
        } catch (IOException | JSONException e) {
            return null;
        }
    };

    public static JsonObject convertToJson(Response response) {
        String data;
        try {
            data = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            JsonObject json = new JsonObject();
            json.addProperty("message", "Error getting data.");
            return json;
        }
        try {
            return (JsonObject) JsonParser.parseString(data);
        } catch (JsonSyntaxException e) {
            JsonObject json = new JsonObject();
            json.addProperty("message", data);
            return json;
        }
    }

    public static <T> T convertToObject(JsonElement json, Class<T> tClass) {
        try {
            return gson.fromJson(json, tClass);
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    /**
     * `/hello` endpoint.
     *
     * @return Response from server.
     */
    @NonNull
    public static CompletableFuture<Response> helloWorld() {
        return client.runAsync(new Request.Builder()
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

        return client.runAsync(new Request.Builder()
                .url(UrlPath.newBuilder()
                        .addPathSegment(UrlPath.ACCOUNT)
                        .addPathSegment(UrlPath.REGISTER)
                        .build())
                .post(RequestBody.create(payload, JSON_TYPE))
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

        return client.runAsync(new Request.Builder()
                .url(UrlPath.newBuilder()
                        .addPathSegment(UrlPath.ACCOUNT)
                        .addPathSegment(UrlPath.LOGIN)
                        .build())
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
        return client.runAsync(new Request.Builder()
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
        return client.runAsync(new Request.Builder()
                .url(UrlPath.newBuilder()
                        .addPathSegment(UrlPath.USERS)
                        .addPathSegment(String.valueOf(userId))
                        .build())
                .header("Authorization", "Bearer " + accessToken)
                .get()
                .build());
    }
}
