package sg.edu.np.mad.Sharecipe.ui;

import android.app.Application;

import com.jakewharton.threetenabp.AndroidThreeTen;

import sg.edu.np.mad.Sharecipe.data.AccountManager;
import sg.edu.np.mad.Sharecipe.data.RecipeManager;
import sg.edu.np.mad.Sharecipe.data.SearchManager;
import sg.edu.np.mad.Sharecipe.data.UserManager;

public class App extends Application {

    private static AccountManager accountManager;
    private static RecipeManager recipeManager;
    private static SearchManager searchManager;
    private static UserManager userManager;

    public static AccountManager getAccountManager() {
        return accountManager;
    }

    public static RecipeManager getRecipeManager() {
        return recipeManager;
    }

    public static SearchManager getSearchManager() {
        return searchManager;
    }

    public static UserManager getUserManager() {
        return userManager;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        AndroidThreeTen.init(App.this);

        accountManager = AccountManager.getInstance(App.this);
        recipeManager = RecipeManager.getInstance(App.this);
        searchManager = SearchManager.getInstance(App.this);
        userManager = UserManager.getInstance(App.this);
    }
}
