package sg.edu.np.mad.Sharecipe.web;

import com.google.gson.JsonElement;

import java.util.HashMap;
import java.util.Map;

import java9.util.concurrent.CompletableFuture;
import java9.util.function.BiConsumer;
import java9.util.function.Consumer;
import okhttp3.Response;
import sg.edu.np.mad.Sharecipe.utils.DataResult;
import sg.edu.np.mad.Sharecipe.utils.FutureDataResult;
import sg.edu.np.mad.Sharecipe.utils.JsonUtils;

/**
 * Wraps the async response of a web request into a {@link CompletableFuture} with additional
 * methods to make life easier.
 */
public class FutureWebResponse extends CompletableFuture<Response> {

    public static FutureWebResponse failedFuture(Throwable throwable) {
        FutureWebResponse future = new FutureWebResponse();
        future.completeExceptionally(throwable);
        return future;
    }

    public FutureWebResponse onSuccess(Consumer<Response> consumer) {
        thenAccept(response -> {
            if (response.isSuccessful()) {
                consumer.accept(response);
            }
        });
        return this;
    }

    public FutureWebResponse onSuccessJson(FutureDataResult<?> future, BiConsumer<Response, JsonElement> consumer) {
        onSuccess(response -> {
            JsonElement json = JsonUtils.convertToJson(response);
            if (json == null) {
                future.complete(new DataResult.Failed<>("Received invalid json data."));
                return;
            }
            consumer.accept(response, json);
        });
        return this;
    }

    public <T> FutureWebResponse onSuccessModel(FutureDataResult<?> future, Class<T> tClass, BiConsumer<Response, T> consumer) {
        onSuccessJson(future, (response, json) -> {
            T object = JsonUtils.convertToObject(json, tClass);
            if (object == null) {
                future.complete(new DataResult.Failed<>("Modeling failed."));
            }
            consumer.accept(response, object);
        });
        return this;
    }

    public FutureWebResponse onFailed(Consumer<Response> consumer) {
        thenAccept(response -> {
            if (!response.isSuccessful()) {
                consumer.accept(response);
            }
        });
        return this;
    }

    public FutureWebResponse onFailed(FutureDataResult<?> future) {
        onFailed(response -> {

            JsonElement json = JsonUtils.convertToJson(response);
            Map<String, Object> objectMap;
            if (json == null) {
                objectMap = new HashMap<String, Object>() {{
                    put("message", "Unknown issue occurred.");
                }};
            } else {
                objectMap = JsonUtils.convertToMap(json);
            }
            objectMap.put("response_code", response.code());
            future.complete(new DataResult.Failed<>(objectMap));
        });
        return this;
    }

    public FutureWebResponse onError(Consumer<Throwable> consumer) {
        exceptionally(throwable -> {
            consumer.accept(throwable);
            return null;
        });
        return this;
    }

    public FutureWebResponse onError(FutureDataResult<?> future) {
        exceptionally(throwable -> {
            future.complete(new DataResult.Error<>(throwable));
            return null;
        });
        return this;
    }

    @Override
    public <U> CompletableFuture<U> newIncompleteFuture() {
        return (CompletableFuture<U>) new FutureWebResponse();
    }
}
