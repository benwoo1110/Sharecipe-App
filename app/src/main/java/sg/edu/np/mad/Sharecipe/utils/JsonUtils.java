package sg.edu.np.mad.Sharecipe.utils;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;

import okhttp3.Response;
import okhttp3.ResponseBody;

public class JsonUtils {

    private static final Gson GSON = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
            .create();

    /**
     * Converts an object model into json element.
     *
     * @param object    Target object to convert.
     * @return The json element, null if an error occurred.
     */
    public static JsonElement convertToJson(Object object) {
        return convertToJson(GSON.toJson(object));
    }

    /**
     * Converts formatted string into json element.
     *
     * @param data  Target string data to convert.
     * @return The json element, null if an error occurred.
     */
    public static JsonElement convertToJson(String data) {
        return JsonParser.parseString(data);
    }

    /**
     * Converts web response data into json element.
     *
     * @param response  Target web response to convert
     * @return The json element, null if an error occurred.
     */
    public static JsonElement convertToJson(Response response) {
        String data;
        try {
            ResponseBody body = response.body();
            if (body == null) {
                return null;
            }
            data = body.string();
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

    /**
     * Convert json element to object model.
     *
     * @param json      The json data with object information.
     * @param tClass    The object class.
     * @param <T>       The object class type.
     * @return The object, null if an error occurred.
     */
    public static <T> T convertToObject(JsonElement json, Class<T> tClass) {
        try {
            return GSON.fromJson(json, tClass);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }
}
