package sg.edu.np.mad.Sharecipe.utils;

import java.util.HashMap;
import java.util.Map;
import java9.util.Optional;

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

        /**
         * Gets resulting data.
         *
         * @return The data object.
         */
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
        private final Map<String, Object> messages;

        public Failed(Failed<?> otherFailed) {
            this.messages = otherFailed.messages;
        }

        public Failed(Map<String, Object> messages) {
            this.messages = messages;
        }

        public Failed(String reason) {
            messages = new HashMap<String, Object>() {{
                put("message", reason);
            }};
        }

        public String getMessage() {
            return messages.containsKey("message") ? String.valueOf(messages.get("message")) : "Unknown issue occurred.";
        }

        public <C> Optional<C> get(String key, Class<C> cClass) {
            try {
                return Optional.ofNullable(cClass.cast(messages.get("message")));
            } catch (ClassCastException e) {
                return Optional.empty();
            }
        }

        /**
         * The reason for the issue to occur.
         *
         * @return The reason.
         */
        @Deprecated
        public String getReason() {
            return getMessage();
        }
    }

    /**
     * An unexpected exception occurred.
     *
     * @param <T>   Data type to get if result successful.
     */
    public static class Error<T> extends DataResult<T> {
        private final Throwable error;

        public Error(Throwable error) {
            this.error = error;
        }

        /**
         * The exception that was throw while trying to get the data.
         *
         * @return The error object.
         */
        public Throwable getError() {
            return this.error;
        }
    }
}
