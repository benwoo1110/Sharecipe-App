package sg.edu.np.mad.Sharecipe.web;

import okhttp3.HttpUrl;

public class UrlPath {
    public static final String SCHEME = "https";
    public static final String HOST = "sharecipe-backend.herokuapp.com";
    public static final String HELLO = "hello";
    public static final String ACCOUNT = "account";
    public static final String USERS = "users";
    public static final String RECIPES = "recipes";

    public static HttpUrl.Builder newBuilder() {
        return new HttpUrl.Builder().scheme(SCHEME).host(HOST);
    }
}
