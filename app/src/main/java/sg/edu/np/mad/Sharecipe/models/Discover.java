package sg.edu.np.mad.Sharecipe.models;

import com.google.gson.annotations.Expose;

import java.util.List;

public class Discover {

    @Expose
    private List<DiscoverSection> sections;

    public List<DiscoverSection> getSections() {
        return sections;
    }

    @Override
    public String toString() {
        return "Discover{" +
                "sections=" + sections +
                '}';
    }
}
