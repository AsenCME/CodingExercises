package pgdp.stream;

abstract class ChainedStreamOperation<T, R> implements StreamOperation<T> {
    private final StreamOperation<R> downstream;

    ChainedStreamOperation(StreamOperable<R> _operable) {
        downstream = _operable.getStreamOperation();
    }

    StreamOperation<R> downstream() {
        return downstream;
    }
}