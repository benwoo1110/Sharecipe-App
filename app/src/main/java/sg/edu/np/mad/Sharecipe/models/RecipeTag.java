package sg.edu.np.mad.Sharecipe.models;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class RecipeTag implements Serializable {

    @Expose
    private String name;

    public RecipeTag() {
    }

    public RecipeTag(String name) {
        this.name = name;
    }

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
