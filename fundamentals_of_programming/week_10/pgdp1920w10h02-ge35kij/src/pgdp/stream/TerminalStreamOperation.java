package pgdp.stream;

import java.util.function.Supplier;

interface TerminalStreamOperation<T, R> extends StreamOperation<T>, Supplier<R> {
    @Override
    default boolean needsMoreElements() {
        return true;
    }
}