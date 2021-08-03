package sg.edu.np.mad.Sharecipe.utils;

import java9.util.concurrent.CompletableFuture;
import java9.util.function.Consumer;
import sg.edu.np.mad.Sharecipe.contants.RunMode;

/**
 * Wrapper for {@link CompletableFuture} to provide custom result handling of {@link DataResult}.
 *
 * @param <T> Type of data expected from future result.
 */
public class FutureDataResult<T> extends CompletableFuture<DataResult<T>> {

    /**
     * Shortcut to created an already completed future.
     *
     * @param result    The completed result
     * @param <T>       Completed object type
     * @return The new {@link FutureDataResult} created.
     */
    public static <T> FutureDataResult<T> completed(DataResult<T> result) {
        FutureDataResult<T> future =  new FutureDataResult<>();
        future.complete(result);
        return future;
    }

    /**
     * Shortcut to created an already completed successful future.
     *
     * @param t     The completed object
     * @param <T>   Completed object type
     * @return The new {@link FutureDataResult} created.
     */
    public static <T> FutureDataResult<T> completed(T t) {
        return completed(new DataResult.Success<T>(t));
    }

    public FutureDataResult() {
    }

    /**
     * When result is a {@link DataResult.Success}.
     *
     * @param consumer  Callback on success.
     * @return The same {@link FutureDataResult} for chaining.
     */
    public FutureDataResult<T> onSuccess(Consumer<T> consumer) {
        this.thenAccept(result -> {
            if (result instanceof DataResult.Success) {
                consumer.accept(((DataResult.Success<T>)result).getData());
            }
        });
        return this;
    }

    /**
     * When result is a {@link DataResult.Failed}.
     *
     * @param consumer  Callback on failure.
     * @return The same {@link FutureDataResult} for chaining.
     */
    public FutureDataResult<T> onFailed(Consumer<DataResult.Failed<T>> consumer) {
        this.thenAccept(result -> {
            if (result instanceof DataResult.Failed) {
                consumer.accept((DataResult.Failed<T>) result);
            }
        });
        return this;
    }

    /**
     * Shortcut to pass failed result from one future to another future.
     *
     * @param otherFuture   Target future to pass result to.
     * @return The same {@link FutureDataResult} for chaining.
     */
    public FutureDataResult<T> onFailed(FutureDataResult<?> otherFuture) {
        onFailed(failedReason -> otherFuture.complete(new DataResult.Failed<>(failedReason)));
        return this;
    }

    /**
     * When result is a {@link DataResult.Error}.
     *
     * @param consumer  Callback on error thrown.
     * @return The same {@link FutureDataResult} for chaining.
     */
    public FutureDataResult<T> onError(Consumer<DataResult.Error<T>> consumer) {
        this.thenAccept(result -> {
            if (result instanceof DataResult.Error) {
                consumer.accept((DataResult.Error<T>) result);
            }
        });
        return this;
    }

    /**
     * Shortcut to pass error result from one future to another future.
     *
     * @param otherFuture   Target future to pass result to.
     * @return The same {@link FutureDataResult} for chaining.
     */
    public FutureDataResult<T> onError(FutureDataResult<?> otherFuture) {
        onError(errorResult -> otherFuture.complete(new DataResult.Error<>(errorResult)));
        return this;
    }

    /**
     * When result is successful, i.e. can be either {@link DataResult.Failed} or {@link DataResult.Error}.
     *
     * @param consumer  Callback on result.
     * @return The same {@link FutureDataResult} for chaining.
     */
    public FutureDataResult<T> onFailedOrError(Consumer<DataResult<T>> consumer) {
        this.thenAccept(result -> {
            if (!(result instanceof DataResult.Success)) {
                consumer.accept(result);
            }
        });
        printErrorForDebug();
        return this;
    }

    /**
     * When result is instance of specified type.
     *
     * @param rClass    CLass of {@link DataResult} to trigger class type.
     * @param consumer  Callback function.
     * @param <R>       Type of {@link DataResult} to trigger class type.
     * @return The same {@link FutureDataResult} for chaining.
     */
    public <R extends DataResult> FutureDataResult<T> acceptResultType(Class<R> rClass, Consumer<R> consumer) {
        this.thenAccept(result -> {
            if (rClass.isInstance(result)) {
                consumer.accept(rClass.cast(result));
            }
        });
        return this;
    }

    @Override
    public <U> CompletableFuture<U> newIncompleteFuture() {
        return (CompletableFuture<U>) new FutureDataResult<>();
    }

    @SuppressWarnings("ThrowableNotThrown")
    private void printErrorForDebug() {
        if (RunMode.IS_PRODUCTION) {
            return;
        }
        onFailed(failedResult -> System.out.println("FAILED RESULT: " + failedResult.getMessages()));
        onError(errorResult -> errorResult.getError().printStackTrace());
    }
}
