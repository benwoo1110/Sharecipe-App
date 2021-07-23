package sg.edu.np.mad.Sharecipe.utils;

import com.google.common.reflect.TypeToken;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import org.threeten.bp.Duration;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Helper methods to facilitate conversion to and from json formats.
 */
public class JsonUtils {

    @SuppressWarnings("UnstableApiUsage")
    private static final Type MAP_TYPE = new TypeToken<Map<String, Object>>() {}.getType();
    private static final Gson GSON = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
            .registerTypeAdapter(Duration.class, new DurationParser())
            .create();

    /**
     * Converts an object model into json string.
     *
     * @param object    Target object to convert.
     * @return The json string, null if an error occurred.
     */
    public static String convertToJsonString(Object object) {
        return GSON.toJson(object);
    }

    /**
     * Converts an object model into json element.
     *
     * @param object    Target object to convert.
     * @return The json element, null if an error occurred.
     */
    public static JsonElement convertToJson(Object object) {
        return convertToJson(convertToJsonString(object));
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

    public static Map<String, Object> convertToMap(JsonElement json) {
        return GSON.fromJson(json, MAP_TYPE);
    }
}
