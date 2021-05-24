package sg.edu.np.mad.Sharecipe.utils;

import java9.util.concurrent.CompletableFuture;
import java9.util.function.Consumer;

/**
 * Wrapper for {@link CompletableFuture} to provide custom result handling of {@link DataResult}.
 *
 * @param <T> Type of data expected from future result.
 */
public class FutureDataResult<T> extends CompletableFuture<DataResult<T>> {

    public FutureDataResult<T> onSuccess(Consumer<T> consumer) {
        this.thenAccept(result -> {
            if (result instanceof DataResult.Success) {
                consumer.accept(((DataResult.Success<T>)result).getData());
            }
        });
        return this;
    }

    public FutureDataResult<T> onFailed(Consumer<String> consumer) {
        this.thenAccept(result -> {
            if (result instanceof DataResult.Failed) {
                consumer.accept(((DataResult.Failed<T>)result).getReason());
            }
        });
        return this;
    }

    public FutureDataResult<T> onError(Consumer<Throwable> consumer) {
        this.thenAccept(result -> {
            if (result instanceof DataResult.Error) {
                consumer.accept(((DataResult.Error<T>)result).getError());
            }
        });
        return this;
    }

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
