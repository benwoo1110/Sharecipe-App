package sg.edu.np.mad.Sharecipe.models;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class RecipeImage implements Serializable {

    @Expose
    private String fileId;
    @Expose
    private int recipeId;

    private RecipeImage() { }

    public String getFileId() {
        return fileId;
    }

    public int getRecipeId() {
        return recipeId;
    }

    @Override
    public String toString() {
        return "RecipeImage{" +
                "fileId='" + fileId + '\'' +
                ", recipeId=" + recipeId +
                '}';
    }
}
