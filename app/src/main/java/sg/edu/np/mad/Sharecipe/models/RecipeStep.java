package sg.edu.np.mad.Sharecipe.models;

public class RecipeStep {

    private int stepNumber;
    private String name;
    private String description;

    public RecipeStep(int stepNumber, String name, String description) {
        this.stepNumber = stepNumber;
        this.name = name;
        this.description = description;
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
                "stepNumber=" + stepNumber +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
