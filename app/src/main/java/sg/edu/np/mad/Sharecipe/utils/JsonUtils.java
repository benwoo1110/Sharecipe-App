package sg.edu.np.mad.Sharecipe.utils;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.text.DateFormat;

import okhttp3.Response;

public class JsonUtils {

    private static final Gson GSON = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
            .create();

    public static JsonElement convertToJson(Object object) {
        return convertToJson(GSON.toJson(object));
    }

    public static JsonElement convertToJson(String data) {
        return JsonParser.parseString(data);
    }

    public static JsonElement convertToJson(Response response) {
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
            return JsonParser.parseString(data);
        } catch (JsonSyntaxException e) {
            JsonObject json = new JsonObject();
            json.addProperty("message", data);
            return json;
        }
    }

    public static <T> T convertToObject(JsonElement json, Class<T> tClass) {
        try {
            return GSON.fromJson(json, tClass);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }
}
