package sg.edu.np.mad.Sharecipe.models;

import com.google.gson.annotations.Expose;

import java.util.List;

public class SearchResult {

    @Expose
    private List<User> users;
    @Expose
    private List<PartialRecipe> recipes;

    public List<User> getUsers() {
        return users;
    }

    public List<PartialRecipe> getRecipes() {
        return recipes;
    }

    @Override
    public String toString() {
        return "SearchResult{" +
                "users=" + users +
                ", recipes=" + recipes +
                '}';
    }
}
