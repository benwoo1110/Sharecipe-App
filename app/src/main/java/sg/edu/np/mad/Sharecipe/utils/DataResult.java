package sg.edu.np.mad.Sharecipe.utils;

/**
 * Result of an action that produces a data.
 *
 * @param <T>   Data type to get if result successful.
 */
public abstract class DataResult<T> {

    /**
     * When data is successfully received.
     *
     * @param <T>   Data type to get if result successful.
     */
    public static class Success<T> extends DataResult<T> {
        private final T data;

        public Success(T data) {
            this.data = data;
        }

        public T getData() {
            return data;
        }
    }

    /**
     * Failed to get data due to issue with input.
     *
     * @param <T>   Data type to get if result successful.
     */
    public static class Failed<T> extends DataResult<T> {
        private final String reason;

        public Failed(String reason) {
            this.reason = reason;
        }

        public String getReason() {
            return reason;
        }
    }

    /**
     * An exception occurred.
     *
     * @param <T>   Data type to get if result successful.
     */
    public static class Error<T> extends DataResult<T> {
        private final Throwable error;

        public Error(Throwable error) {
            this.error = error;
        }

        public Throwable getError() {
            return this.error;
        }
    }
}
