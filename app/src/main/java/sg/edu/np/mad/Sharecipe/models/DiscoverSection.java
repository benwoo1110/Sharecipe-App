package sg.edu.np.mad.Sharecipe.models;

import com.google.gson.annotations.Expose;

import java.util.List;

import sg.edu.np.mad.Sharecipe.ui.main.discover.SizeType;

public class DiscoverSection {

    @Expose
    private String header;
    @Expose
    private SizeType size;
    @Expose
    private List<PartialRecipe> recipes;

    public String getHeader() {
        return header;
    }

    public SizeType getSizeType() {
        return size;
    }

    public List<PartialRecipe> getRecipes() {
        return recipes;
    }

    @Override
    public String toString() {
        return "DiscoverSection{" +
                "header='" + header + '\'' +
                ", size=" + size +
                ", recipes=" + recipes +
                '}';
    }
}
