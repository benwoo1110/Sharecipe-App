package sg.edu.np.mad.Sharecipe.contants;

import okhttp3.HttpUrl;
import sg.edu.np.mad.Sharecipe.BuildConfig;

/**
 * Stores possible endpoints for easy reference and url building.
 */
public class UrlPath {

    private static final boolean DEBUG_MODE = BuildConfig.BUILD_TYPE.equals("debug");

    public static final String SCHEME = "https";
    public static final String HOST = DEBUG_MODE ? "sharecipe-backend.herokuapp.com" : "sharecipe-production.herokuapp.com";
    public static final int PORT = 443;
    public static final String HELLO = "hello";
    public static final String ACCOUNT = "account";
    public static final String REGISTER = "register";
    public static final String LOGIN = "login";
    public static final String REFRESH = "refresh";
    public static final String CHANGE_PASSWORD = "changepassword";
    public static final String LOGOUT = "logout";
    public static final String DELETE = "delete";
    public static final String USERS = "users";
    public static final String PROFILE_IMAGE = "profileimage";
    public static final String FOLLOWS = "follows";
    public static final String FOLLOWERS = "followers";
    public static final String LIKES = "likes";
    public static final String RECIPES = "recipes";
    public static final String IMAGES = "images";
    public static final String ICON = "icon";
    public static final String SEARCH = "search";
    public static final String DISCOVER = "discover";

    /**
     * Creates new url builder instance with default host target.
     *
     * @return The url builder.
     */
    public static HttpUrl.Builder newBuilder() {
        return new HttpUrl.Builder().scheme(SCHEME).host(HOST).port(PORT);
    }
}
