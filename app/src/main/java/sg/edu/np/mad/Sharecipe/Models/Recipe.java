package sg.edu.np.mad.Sharecipe.Models;

import java.time.Duration;
import java.util.List;

public class Recipe {

    private int userId;
    private int recipeId;
    private String name;
    private String cuisineType;
    private String foodType;
    private boolean hidden;
    private int difficulty;
    private int portionMin;
    private int portionMax;
    private Duration prepareTime;
    private List<RecipeStep> steps;
}
