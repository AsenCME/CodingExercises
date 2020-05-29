package pgdp.stream;

abstract class IntermediatePart<IN, OUT> extends AbstractStreamPart<IN, OUT> {
    AbstractStreamPart<?, IN> previous;

    public IntermediatePart(AbstractStreamPart<?, IN> _previous) {
        previous = _previous;
        previous.next = this;
    }

    @Override
    SourcePart<?> getSource() {
        return previous.getSource();
    }
}