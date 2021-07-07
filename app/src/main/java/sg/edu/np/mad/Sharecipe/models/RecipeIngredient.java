package sg.edu.np.mad.Sharecipe.models;

import com.google.gson.annotations.Expose;

public class RecipeIngredient {

    @Expose
    private int recipeId;
    @Expose
    private String name;
    @Expose
    private int quantity;
    @Expose
    private String unit;

    public RecipeIngredient() { }

    public int getRecipeId() {
        return recipeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "RecipeIngredient{" +
                "name='" + name + '\'' +
                ", quantity=" + quantity +
                ", unit='" + unit + '\'' +
                '}';
    }
}
