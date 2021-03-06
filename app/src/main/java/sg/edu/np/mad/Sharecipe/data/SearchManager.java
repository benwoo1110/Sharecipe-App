package sg.edu.np.mad.Sharecipe.data;

import android.content.Context;

import sg.edu.np.mad.Sharecipe.models.Discover;
import sg.edu.np.mad.Sharecipe.models.DiscoverSection;
import sg.edu.np.mad.Sharecipe.models.Recipe;
import sg.edu.np.mad.Sharecipe.models.SearchResult;
import sg.edu.np.mad.Sharecipe.models.User;
import sg.edu.np.mad.Sharecipe.utils.DataResult;
import sg.edu.np.mad.Sharecipe.utils.FutureDataResult;
import sg.edu.np.mad.Sharecipe.web.SharecipeRequests;

/**
 * Contains actions for generally getting of large data.
 */
public class SearchManager {

    private static SearchManager instance;

    /**
     * Gets common {@link SearchManager} instance throughout the app.
     *
     * @param context   Application context for share preference loading.
     * @return The instance.
     */
    public static SearchManager getInstance(Context context) {
        if (instance == null) {
            instance = new SearchManager(
                    AccountManager.getInstance(context.getApplicationContext()),
                    UserManager.getInstance(context),
                    RecipeManager.getInstance(context)
            );
        }
        return instance;
    }

    private final AccountManager accountManager;
    private final UserManager userManager;
    private final RecipeManager recipeManager;

    public SearchManager(AccountManager accountManager, UserManager userManager, RecipeManager recipeManager) {
        this.accountManager = accountManager;
        this.userManager = userManager;
        this.recipeManager = recipeManager;
    }

    /**
     * Search for recipes and users based on server defined parameters.
     *
     * @param searchQuery   String filter.
     * @return Future result of search.
     */
    public FutureDataResult<SearchResult> search(String searchQuery) {
        FutureDataResult<SearchResult> future = new FutureDataResult<>();

        accountManager.getOrRefreshAccount().onSuccess(account -> {
            SharecipeRequests.getSearch(account.getAccessToken(), searchQuery).onSuccessModel(future, SearchResult.class, (response, searchResult) -> {
                for (User user : searchResult.getUsers()) {
                    userManager.addToCache(user);
                }
                for (Recipe recipe : searchResult.getRecipes()) {
                    recipeManager.addToCache(recipe);
                }
                future.complete(new DataResult.Success<>(searchResult));
            }).onFailed(future).onError(future);
        }).onFailed(future).onError(future);

        return future;
    }

    public FutureDataResult<Discover> discover() {
        FutureDataResult<Discover> future = new FutureDataResult<>();

        accountManager.getOrRefreshAccount().onSuccess(account -> {
            SharecipeRequests.getDiscover(account.getAccessToken()).onSuccessModel(future, Discover.class, (response, discover) -> {
                for (DiscoverSection section : discover.getSections()) {
                    for (Recipe recipe : section.getRecipes()) {
                        recipeManager.addToCache(recipe);
                    }
                }
                future.complete(new DataResult.Success<>(discover));
            }).onFailed(future).onError(future);
        }).onFailed(future).onError(future);

        return future;
    }
}
