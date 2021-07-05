package sg.edu.np.mad.Sharecipe.models;

import com.google.gson.annotations.Expose;

public class RecipeStep {

    @Expose
    private int recipeId;
    @Expose
    private int stepNumber;
    @Expose
    private String name;
    @Expose
    private String description;
    //TODO timeNeeded

    public RecipeStep(int stepNumber, String name, String description) {
        this.stepNumber = stepNumber;
        this.name = name;
        this.description = description;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
