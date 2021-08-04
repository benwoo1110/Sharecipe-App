package sg.edu.np.mad.Sharecipe.models;

import com.google.gson.annotations.Expose;

import sg.edu.np.mad.Sharecipe.ui.main.profile.stats.StatsType;

public class Stats {

    @Expose
    private String name;
    @Expose
    private int number;
    @Expose
    private StatsType statsType;

    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }

    public StatsType getStatsType() {
        return statsType;
    }

    @Override
    public String toString() {
        return "Stats{" +
                "name='" + name + '\'' +
                ", number=" + number +
                ", statsType=" + statsType +
                '}';
    }
}
