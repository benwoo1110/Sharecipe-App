package sg.edu.np.mad.Sharecipe.models;

import com.google.gson.annotations.Expose;

public class RecipeImage {
    @Expose
    private String fileId;

    private RecipeImage() { }

    public String getFileId() {
        return fileId;
    }
}
