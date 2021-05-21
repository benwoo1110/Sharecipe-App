package sg.edu.np.mad.Sharecipe.utils;

public abstract class ActionResult {

    public static final ActionResult GENERIC_SUCCESS = new ActionResult.Success();
    public static final ActionResult GENERIC_ERROR = new ActionResult.Error("An Error occurred ;(");

    public static class Success extends ActionResult {

    }

    public static class Error extends ActionResult {
        private final String errorMessage;

        public Error(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }
}
