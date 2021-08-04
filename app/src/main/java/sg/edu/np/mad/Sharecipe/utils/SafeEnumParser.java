package sg.edu.np.mad.Sharecipe.utils;

import androidx.annotation.NonNull;

import com.google.common.base.Strings;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class SafeEnumParser<T extends Enum<T>> implements JsonDeserializer<T>, JsonSerializer<T> {

    private final Class<T> eClass;
    private final T defaultValue;

    public SafeEnumParser(@NonNull Class<T> eClass, @NonNull T defaultValue) {
        this.eClass = eClass;
        this.defaultValue = defaultValue;
    }

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String s = json.getAsString();
        if (Strings.isNullOrEmpty(s)) {
            return defaultValue;
        }
        try {
            return Enum.valueOf(eClass, s.toUpperCase());
        } catch (IllegalArgumentException e) {
            return defaultValue;
        }
    }

    @Override
    public JsonElement serialize(Enum src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString().toLowerCase());
    }
}
