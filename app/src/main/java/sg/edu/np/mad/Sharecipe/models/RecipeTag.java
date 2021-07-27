package sg.edu.np.mad.Sharecipe.models;

import com.google.gson.annotations.Expose;

public class RecipeTag {

    @Expose
    private int name;

    public int getName() { return name; }

    @Override
    public String toString() {
        return "RecipeTag(" +
                "name=" + name +
                '}';
    }
}
