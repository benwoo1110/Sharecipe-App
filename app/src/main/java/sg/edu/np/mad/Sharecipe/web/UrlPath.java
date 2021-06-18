package sg.edu.np.mad.Sharecipe.web;

import okhttp3.HttpUrl;

/**
 * Stores possible endpoints for easy reference and url building.
 */
public class UrlPath {
    //TODO remove on production.
    private static final boolean USER_LOCAL = false;

    public static final String SCHEME = USER_LOCAL ? "http" : "https";
    public static final String HOST = USER_LOCAL ? "10.0.2.2" : "sharecipe-backend.herokuapp.com";
    public static final int PORT = USER_LOCAL ? 5000 : 443;
    public static final String HELLO = "hello";
    public static final String ACCOUNT = "account";
    public static final String REGISTER = "register";
    public static final String LOGIN = "login";
    public static final String REFRESH = "refresh";
    public static final String LOGOUT = "logout";
    public static final String DELETE = "delete";
    public static final String USERS = "users";
    public static final String RECIPES = "recipes";

    public static HttpUrl.Builder newBuilder() {
        return new HttpUrl.Builder().scheme(SCHEME).host(HOST).port(PORT);
    }
}
