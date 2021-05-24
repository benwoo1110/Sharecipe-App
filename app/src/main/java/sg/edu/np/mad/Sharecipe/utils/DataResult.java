package sg.edu.np.mad.Sharecipe.utils;

public abstract class DataResult<T> {

    public static class Success<T> extends DataResult<T> {
        private final T data;

        public Success(T data) {
            this.data = data;
        }

        public T getData() {
            return data;
        }
    }

    public static class Failed<T> extends DataResult<T> {
        private final String reason;

        public Failed(String reason) {
            this.reason = reason;
        }

        public String getReason() {
            return reason;
        }
    }

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
