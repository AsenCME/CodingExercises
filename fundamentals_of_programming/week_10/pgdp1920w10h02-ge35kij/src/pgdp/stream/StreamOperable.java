package pgdp.stream;

@FunctionalInterface
interface StreamOperable<T> {
    StreamOperation<T> getStreamOperation();
}