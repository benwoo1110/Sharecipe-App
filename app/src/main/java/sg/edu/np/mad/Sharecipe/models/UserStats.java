package sg.edu.np.mad.Sharecipe.models;

import com.google.gson.annotations.Expose;

public class UserStats {

    @Expose
    private String name;
    @Expose
    private int number;

    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return "UserStats{" +
                "name='" + name + '\'' +
                ", number=" + number +
                '}';
    }
}
