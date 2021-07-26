package sg.edu.np.mad.Sharecipe.models;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class RecipeStep implements Serializable {

    @Expose
    private int recipeId;
    @Expose
    private int stepNumber;
    @Expose
    private String description;

    public RecipeStep() { }

    public int getRecipeId() {
        return recipeId;
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "RecipeStep{" +
                "recipeId=" + recipeId +
                ", stepNumber=" + stepNumber +
                ", description='" + description + '\'' +
                '}';
    }
}
