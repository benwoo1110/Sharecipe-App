package sg.edu.np.mad.Sharecipe.utils;

import java9.util.concurrent.CompletableFuture;
import java9.util.function.Consumer;

/**
 * Wrapper for {@link CompletableFuture} to provide custom result handling of {@link DataResult}.
 *
 * @param <T> Type of data expected from future result.
 */
public class FutureDataResult<T> extends CompletableFuture<DataResult<T>> {

    public static <T> FutureDataResult<T> completed(DataResult<T> result) {
        FutureDataResult<T> future =  new FutureDataResult<>();
        future.complete(result);
        return future;
    }

    public static <T> FutureDataResult<T> completed(T t) {
        return completed(new DataResult.Success<T>(t));
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
    public FutureDataResult<T> onFailed(Consumer<String> consumer) {
        this.thenAccept(result -> {
            if (result instanceof DataResult.Failed) {
                consumer.accept(((DataResult.Failed<T>)result).getReason());
            }
        });
        return this;
    }

    public FutureDataResult<T> onFailed(FutureDataResult<?> otherFuture) {
        onFailed(reason -> otherFuture.complete(new DataResult.Failed<>(reason)));
        return this;
    }

    /**
     * When result is a {@link DataResult.Error}.
     *
     * @param consumer  Callback on error thrown.
     * @return The same {@link FutureDataResult} for chaining.
     */
    public FutureDataResult<T> onError(Consumer<Throwable> consumer) {
        this.thenAccept(result -> {
            if (result instanceof DataResult.Error) {
                consumer.accept(((DataResult.Error<T>)result).getError());
            }
        });
        return this;
    }

    public FutureDataResult<T> onError(FutureDataResult<?> otherFuture) {
        onError(throwable -> otherFuture.complete(new DataResult.Error<>(throwable)));
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
}
