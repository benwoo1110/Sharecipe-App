package sg.edu.np.mad.Sharecipe.models;

import com.google.gson.annotations.Expose;

import org.threeten.bp.Duration;

import java.util.Date;
import java.util.List;

public class Recipe extends PartialRecipe {

    @Expose
    private int portion;
    @Expose
    private int difficulty;
    @Expose
    private Duration totalTimeNeeded;
    @Expose(serialize = false)
    private Date timeCreated;
    @Expose
    private List<RecipeStep> steps;
    @Expose
    private List<RecipeIngredient> ingredients;
    @Expose
    private List<RecipeImage> images;

    public Recipe() { }

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

    public Duration getTotalTimeNeeded() {
        return totalTimeNeeded == null ? Duration.ZERO : totalTimeNeeded;
    }

    public void setTotalTimeNeeded(Duration totalTimeNeeded) {
        this.totalTimeNeeded = totalTimeNeeded;
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

    public List<RecipeIngredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<RecipeIngredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<RecipeImage> getImages() {
        return images;
    }

    public void setImages(List<RecipeImage> images) {
        this.images = images;
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
