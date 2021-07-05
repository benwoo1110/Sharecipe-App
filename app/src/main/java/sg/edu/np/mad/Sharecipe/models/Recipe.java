package sg.edu.np.mad.Sharecipe.models;

import com.google.gson.annotations.Expose;

import java.util.Date;
import java.util.List;

public class Recipe {

    @Expose
    private int userId;
    @Expose
    private int recipeId;
    @Expose
    private String name;
    @Expose
    private int portion;
    @Expose
    private int difficulty;
    @Expose(serialize = false)
    private Date timeCreated;
    @Expose
    private List<RecipeStep> steps;

    public Recipe() { }

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

    public int getPortion() {
        return portion;
    }

    public void setPortion(int portion) {
        this.portion = portion;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public Date getTimeCreated() {
        return timeCreated;
    }

    public List<RecipeStep> getSteps() {
        return steps;
    }

    public void setSteps(List<RecipeStep> steps) {
        this.steps = steps;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "userId=" + userId +
                ", recipeId=" + recipeId +
                ", name='" + name + '\'' +
                ", portion=" + portion +
                ", difficulty=" + difficulty +
                ", timeCreated=" + timeCreated +
                ", steps=" + steps +
                '}';
    }
}
