package sg.edu.np.mad.Sharecipe.utils;

public class Interval {

    private final int interval;
    private long lastRun;

    public Interval(int interval) {
        this.interval = interval;
        this.lastRun = 0;
    }

    public boolean check() {
        return (System.currentTimeMillis() - lastRun) > interval;
    }

    public boolean update() {
        if (check()) {
            this.lastRun = System.currentTimeMillis();
            return true;
        }
        return false;
    }
}
