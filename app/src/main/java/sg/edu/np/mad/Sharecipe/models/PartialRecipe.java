package sg.edu.np.mad.Sharecipe.models;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class PartialRecipe implements Serializable {

    @Expose
    protected int userId;
    @Expose
    protected int recipeId;
    @Expose
    protected String name;
    @Expose
    protected ImageRef icon;

    public int getUserId() {
        return userId;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ImageRef getIcon() {
        return icon;
    }

    @Override
    public String toString() {
        return "PartialRecipe{" +
                "userId=" + userId +
                ", recipeId=" + recipeId +
                ", name='" + name + '\'' +
                ", icon=" + icon +
                '}';
    }
}
