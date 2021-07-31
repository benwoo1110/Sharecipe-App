package sg.edu.np.mad.Sharecipe.models;

import com.google.gson.annotations.Expose;

public class UserStats {

    @Expose(deserialize = false)
    private String name;
    private int number;

    public UserStats(String name, int number) {
        this.name = name;
        this.number = number;
    }

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
