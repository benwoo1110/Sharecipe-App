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
        try {
            return (JsonObject) JsonParser.parseString(response.body().string());
        } catch (IOException e) {
            return null;
        }
    }

    public static <T> T convertToObject(JsonElement json, Class<T> tClass) {
        try {
            return gson.fromJson(json, tClass);
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    @NonNull
    public static CompletableFuture<Response> helloWorld() {
        HttpUrl url = UrlPath.newBuilder()
                .addPathSegment(UrlPath.HELLO)
                .build();
        System.out.println(url.toString());
        return client.runAsync(new Request.Builder()
                .url(url)
                .get()
                .build());
    }

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
}
