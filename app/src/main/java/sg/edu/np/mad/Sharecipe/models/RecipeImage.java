package sg.edu.np.mad.Sharecipe.models;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeImage that = (RecipeImage) o;
        return recipeId == that.recipeId &&
                Objects.equals(fileId, that.fileId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileId, recipeId);
    }

    @Override
    public String toString() {
        return "RecipeImage{" +
                "fileId='" + fileId + '\'' +
                ", recipeId=" + recipeId +
                '}';
    }
}
