package pgdp.stream;

interface StreamOperation<T> {
    void start(StreamCharacteristics upstreamCharacteristics);

    void acceptElement(StreamElement<T> element);

    void finish();

    boolean needsMoreElements();
}