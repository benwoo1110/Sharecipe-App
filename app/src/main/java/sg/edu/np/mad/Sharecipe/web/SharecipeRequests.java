package sg.edu.np.mad.Sharecipe.web;

import java9.util.concurrent.CompletableFuture;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

public class SharecipeRequests {

    private static final AsyncOkHttpClient client = new AsyncOkHttpClient();

    public static CompletableFuture<Response> helloWorld() {
        HttpUrl url = UrlPath.newBuilder().addPathSegment(UrlPath.HELLO).build();
        return client.runAsync(new Request.Builder()
                .url(url)
                .get()
                .build());
    }
}
