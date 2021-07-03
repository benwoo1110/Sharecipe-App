package sg.edu.np.mad.Sharecipe.utils;

/**
 * Manages an interval between running of things.
 */
public class Interval {

    private final int interval;
    private long lastRun;

    public Interval(int interval) {
        this.interval = interval;
        this.lastRun = 0;
    }

    /**
     * Check if the specified interval has passed.
     *
     * @return True if interval passed, else false.
     */
    public boolean check() {
        return (System.currentTimeMillis() - lastRun) > interval;
    }

    /**
     * Checks and updates last run record if interval passed.
     *
     * @return True if interval passed, else false.
     */
    public boolean update() {
        if (check()) {
            this.lastRun = System.currentTimeMillis();
            return true;
        }
        return false;
    }
}
