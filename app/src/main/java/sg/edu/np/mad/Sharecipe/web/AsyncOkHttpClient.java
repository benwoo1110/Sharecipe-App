package sg.edu.np.mad.Sharecipe.web;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import java9.util.concurrent.CompletableFuture;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Wraps {@link OkHttpClient} get call request data with {@link CompletableFuture}.
 */
public class AsyncOkHttpClient {

    private final OkHttpClient client = new OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .build();

    /**
     * Calls a web request asynchronously
     *
     * @param request   Target request to call.
     * @return Future result of the response.
     */
    public FutureWebResponse runAsync(Request request) {
        FutureWebResponse future = new FutureWebResponse();
        this.client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                future.completeExceptionally(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                future.complete(response);
            }
        });
        return future;
    }

    /**
     * Underlying http client used.
     *
     * @return The http client.
     */
    public OkHttpClient getClient() {
        return client;
    }
}
