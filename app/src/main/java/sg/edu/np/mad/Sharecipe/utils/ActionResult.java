package sg.edu.np.mad.Sharecipe.utils;

public abstract class ActionResult {

    public static final ActionResult GENERIC_SUCCESS = new ActionResult.Success("Success!");
    public static final ActionResult GENERIC_FAILED = new ActionResult.Failed("Failed!");

    private final String message;

    protected ActionResult(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public static class Success extends ActionResult {
        public Success(String message) {
            super(message);
        }
    }

    public static class Failed extends ActionResult {
        public Failed(String message) {
            super(message);
        }
    }

    public static class Error extends ActionResult {
        private final Throwable exception;

        public Error(String message, Throwable exception) {
            super(message);
            this.exception = exception;
        }

        public Error(Throwable exception) {
            super(exception.getMessage());
            this.exception = exception;
        }

        public Throwable getException() {
            return exception;
        }
    }
}
