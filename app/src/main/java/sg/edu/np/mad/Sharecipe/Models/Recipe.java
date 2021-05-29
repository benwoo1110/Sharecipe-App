package sg.edu.np.mad.Sharecipe.Models;

import java.util.List;

public class Recipe {

    private int userId;
    private int recipeId;
    private String name;
    private int portion;
    private int difficulty;
    private List<RecipeStep> steps;

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
                ", steps=" + steps +
                '}';
    }
}
