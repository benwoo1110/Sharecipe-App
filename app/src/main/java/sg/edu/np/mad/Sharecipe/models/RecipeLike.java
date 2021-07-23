package sg.edu.np.mad.Sharecipe.models;

import com.google.gson.annotations.Expose;

public class RecipeLike {

    @Expose
    private int recipeId;
    @Expose
    private int userId;

    public int getRecipeId() {
        return recipeId;
    }

    public int getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "RecipeLike{" +
                "recipeId=" + recipeId +
                ", userId=" + userId +
                '}';
    }
}
