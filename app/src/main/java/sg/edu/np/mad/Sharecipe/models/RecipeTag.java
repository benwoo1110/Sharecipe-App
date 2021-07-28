package sg.edu.np.mad.Sharecipe.models;

import com.google.gson.annotations.Expose;

public class RecipeTag {

    @Expose
    private String name;

    public String getName() { return name; }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "RecipeTag(" +
                "name=" + name +
                '}';
    }
}
