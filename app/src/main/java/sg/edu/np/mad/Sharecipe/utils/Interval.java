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
     * Updates last run record if interval passed.
     */
    public void update() {
        this.lastRun = System.currentTimeMillis();
    }

    /**
     * Reset and forget last run time.
     */
    public void reset() {
        this.lastRun = 0;
    }
}
