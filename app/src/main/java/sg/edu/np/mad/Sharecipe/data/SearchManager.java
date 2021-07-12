package sg.edu.np.mad.Sharecipe.data;

import android.content.Context;

import sg.edu.np.mad.Sharecipe.models.SearchResult;
import sg.edu.np.mad.Sharecipe.utils.DataResult;
import sg.edu.np.mad.Sharecipe.utils.FutureDataResult;
import sg.edu.np.mad.Sharecipe.web.SharecipeRequests;

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
            instance = new SearchManager(AccountManager.getInstance(context.getApplicationContext()));
        }
        return instance;
    }

    private final AccountManager accountManager;

    public SearchManager(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    public FutureDataResult<SearchResult> search(String searchQuery) {
        FutureDataResult<SearchResult> future = new FutureDataResult<>();

        accountManager.getOrRefreshAccount().onSuccess(account -> {
            SharecipeRequests.search(account.getAccessToken(), searchQuery).onSuccessModel(future, SearchResult.class, (response, searchResult) -> {
                future.complete(new DataResult.Success<>(searchResult));
            }).onFailed(future).onError(future);
        }).onFailed(future).onError(future);

        return future;
    }
}