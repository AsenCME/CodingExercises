package pgdp.stream;

class SourcePart<T> extends AbstractStreamPart<T, T> {
    private final StreamIterator<T> iterator;
    private final StreamCharacteristics characteristics;

    private boolean shouldTerminate = false;

    public boolean getShouldTerminate() {
        return shouldTerminate;
    }

    public void setShouldTerminate(boolean value) {
        this.shouldTerminate = value;
    }

    SourcePart(StreamIterator<T> _iterator, StreamCharacteristics _characteristics) {
        iterator = _iterator;
        characteristics = _characteristics;
    }

    @Override
    SourcePart<?> getSource() {
        return this;
    }

    @Override
    public StreamOperation<T> getStreamOperation() {
        return new StreamOperation<T>() {
            StreamOperation<T> nextOperation = next.getStreamOperation();

            @Override
            public void start(StreamCharacteristics upstreamCharacteristics) {
                nextOperation.start(characteristics);
            }

            @Override
            public void acceptElement(StreamElement<T> element) {
                if (needsMoreElements())
                    nextOperation.acceptElement(element);
            }

            @Override
            public void finish() {
                nextOperation.finish();
            }

            @Override
            public boolean needsMoreElements() {
                return nextOperation.needsMoreElements();
            }
        };
    }

    void processStream() {
        StreamOperation<T> nextOperation = getStreamOperation();

        nextOperation.start(characteristics);

        while (!shouldTerminate && iterator.hasNext()) {
            StreamElement<T> element = iterator.next();
            nextOperation.acceptElement(element);
        }

        nextOperation.finish();
    }
}