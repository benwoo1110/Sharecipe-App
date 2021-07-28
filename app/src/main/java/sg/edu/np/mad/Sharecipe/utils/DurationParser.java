package sg.edu.np.mad.Sharecipe.utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.threeten.bp.Duration;

import java.lang.reflect.Type;

/**
 * Handlers conversion between seconds int and {@link Duration} object.
 */
public class DurationParser implements JsonDeserializer<Duration>, JsonSerializer<Duration> {

    @Override
    public Duration deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return Duration.ofSeconds(json.getAsLong());
    }

    @Override
    public JsonElement serialize(Duration src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getSeconds());
    }
}
