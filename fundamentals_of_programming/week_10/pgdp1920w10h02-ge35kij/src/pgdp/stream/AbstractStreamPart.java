package pgdp.stream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

abstract class AbstractStreamPart<IN, OUT> implements Stream<OUT>, StreamOperable<IN> {
    StreamOperable<OUT> next = null;

    void setNext(StreamOperable<OUT> nextOperable) {
        if (next == null)
            throw new IllegalStateException("Incorrect state of the stream: Attribute \"next\" has already been set");
        next = nextOperable;
    }

    abstract SourcePart<?> getSource();

    public abstract StreamOperation<IN> getStreamOperation();

    <R> R evaluate(TerminalStreamOperation<OUT, R> operation) {
        next = new StreamOperable<OUT>() {

            @Override
            public StreamOperation<OUT> getStreamOperation() {
                return operation;
            }
        };
        getSource().processStream();
        return operation.get();
    }

    @Override
    public <R> Stream<R> map(Function<? super OUT, ? extends R> mapper) {
        return new IntermediatePart<OUT, R>(this) {
            @Override
            public StreamOperation<OUT> getStreamOperation() {
                return new StreamOperation<OUT>() {
                    private StreamOperation<R> nextOperation = next.getStreamOperation();

                    @Override
                    public void start(StreamCharacteristics upstreamCharacteristics) {
                        if (mapper == null)
                            throw new NullPointerException("Mapper is null");
                        nextOperation.start(upstreamCharacteristics);
                    }

                    @Override
                    public void acceptElement(StreamElement<OUT> element) {
                        if (element.type == StreamElementType.FAULTY)
                            nextOperation.acceptElement(element.tryAdapt());
                        else {
                            try {
                                StreamElement<R> mappedElement = StreamElement.of(mapper.apply(element.element));
                                nextOperation.acceptElement(mappedElement);
                            } catch (Exception e) {
                                nextOperation.acceptElement(element.withExceptionAdded(e));
                            }
                        }
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
        };
    }

    @Override
    public Stream<OUT> filter(Predicate<? super OUT> filter) {
        return new IntermediatePart<OUT, OUT>(this) {
            @Override
            public StreamOperation<OUT> getStreamOperation() {
                return new StreamOperation<OUT>() {
                    private StreamOperation<OUT> nextOperation = next.getStreamOperation();

                    @Override
                    public void start(StreamCharacteristics upstreamCharacteristics) {
                        if (filter == null)
                            throw new NullPointerException("Filter is null");
                        nextOperation.start(StreamCharacteristics.REGULAR.withChecked(upstreamCharacteristics.isChecked)
                                .withDistinct(upstreamCharacteristics.isDistinct));
                    }

                    @Override
                    public void acceptElement(StreamElement<OUT> element) {
                        if (element.type == StreamElementType.FAULTY)
                            nextOperation.acceptElement(element);
                        else {
                            try {
                                if (filter.test(element.element))
                                    nextOperation.acceptElement(element);
                            } catch (Exception e) {
                                nextOperation.acceptElement(element.withExceptionAdded(e));
                            }
                        }
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
        };
    }

    @Override
    public Stream<OUT> distinct() {
        return new IntermediatePart<OUT, OUT>(this) {
            @Override
            public StreamOperation<OUT> getStreamOperation() {
                return new StreamOperation<OUT>() {
                    private ArrayList<StreamElement<OUT>> resultList;
                    private ArrayList<OUT> distinctList;
                    private StreamOperation<OUT> nextOperation = next.getStreamOperation();

                    @Override
                    public void start(StreamCharacteristics upstreamCharacteristics) {
                        resultList = new ArrayList<StreamElement<OUT>>();
                        distinctList = new ArrayList<OUT>();
                        nextOperation.start(upstreamCharacteristics.withDistinct(true));
                    }

                    @Override
                    public void acceptElement(StreamElement<OUT> element) {
                        if (element.type == StreamElementType.FAULTY)
                            resultList.add(element);
                        else if (!distinctList.contains(element.element)) {
                            resultList.add(element);
                            distinctList.add(element.element);
                        }

                    }

                    @Override
                    public void finish() {
                        getSource().setShouldTerminate(false);

                        for (StreamElement<OUT> el : resultList) {
                            if (getSource().getShouldTerminate())
                                break;
                            nextOperation.acceptElement(el);
                        }
                        nextOperation.finish();
                    }

                    @Override
                    public boolean needsMoreElements() {
                        return true;
                    }
                };
            }
        };
    }

    @Override
    public long count() {
        TerminalStreamOperation<OUT, Long> operation = new TerminalStreamOperation<OUT, Long>() {
            private long counter = 0L;

            @Override
            public void start(StreamCharacteristics upstreamCharacteristics) {
                if (upstreamCharacteristics.isChecked)
                    throw new CheckedStreamException();
                else if (upstreamCharacteristics.streamSize != null) {
                    counter = upstreamCharacteristics.streamSize.getAsLong();
                    getSource().setShouldTerminate(true);
                }
            }

            @Override
            public void acceptElement(StreamElement<OUT> element) {
                if (element.type == StreamElementType.FAULTY)
                    throw new ErrorsAtTerminalOperationException();
                else
                    counter++;
            }

            @Override
            public void finish() {
            }

            @Override
            public Long get() {
                return counter;
            }
        };
        return evaluate(operation);
    }

    @Override
    public Optional<OUT> findFirst() {
        TerminalStreamOperation<OUT, Optional<OUT>> operation = new TerminalStreamOperation<OUT, Optional<OUT>>() {

            private Optional<OUT> firstElement = null;

            @Override
            public void start(StreamCharacteristics upstreamCharacteristics) {
                if (upstreamCharacteristics.isChecked)
                    throw new CheckedStreamException();
            }

            @Override
            public void acceptElement(StreamElement<OUT> element) {
                if (element.type == StreamElementType.FAULTY)
                    throw new ErrorsAtTerminalOperationException();
                else if (element.element == null)
                    throw new NullPointerException();
                else {
                    firstElement = Optional.of(element.element);
                    getSource().setShouldTerminate(true);
                }
            }

            @Override
            public void finish() {
            }

            @Override
            public Optional<OUT> get() {
                return firstElement;
            }
        };
        return evaluate(operation);
    }

    @Override
    public Optional<OUT> reduce(BinaryOperator<OUT> accumulator) {
        TerminalStreamOperation<OUT, Optional<OUT>> operation = new TerminalStreamOperation<OUT, Optional<OUT>>() {
            private Optional<OUT> result = Optional.empty();
            private OUT previousElement = null;

            @Override
            public void start(StreamCharacteristics upstreamCharacteristics) {
                if (accumulator == null)
                    throw new NullPointerException();
                else if (upstreamCharacteristics.isChecked)
                    throw new CheckedStreamException();
            }

            @Override
            public void acceptElement(StreamElement<OUT> element) {
                if (element.type == StreamElementType.FAULTY)
                    throw new ErrorsAtTerminalOperationException();
                else {
                    if (previousElement == null)
                        previousElement = element.element;
                    else
                        previousElement = accumulator.apply(previousElement, element.element);
                    result = Optional.of(previousElement);
                }
            }

            @Override
            public void finish() {
            }

            @Override
            public Optional<OUT> get() {
                if (result == null)
                    throw new NullPointerException();
                return result;
            }
        };
        return evaluate(operation);
    }

    @Override
    public Collection<OUT> toCollection(Supplier<? extends Collection<OUT>> collectionGenerator) {
        TerminalStreamOperation<OUT, Collection<OUT>> operation = new TerminalStreamOperation<OUT, Collection<OUT>>() {
            private Collection<OUT> collection = null;

            @Override
            public void start(StreamCharacteristics upstreamCharacteristics) {
                if (collectionGenerator == null)
                    throw new NullPointerException("CollectionGenerator not provided");
                else if (upstreamCharacteristics.isChecked)
                    throw new CheckedStreamException();
                else
                    collection = collectionGenerator.get();
            }

            @Override
            public void acceptElement(StreamElement<OUT> element) {
                if (element.type == StreamElementType.FAULTY)
                    throw new ErrorsAtTerminalOperationException();
                else
                    collection.add(element.element);
            }

            @Override
            public void finish() {
            }

            @Override
            public Collection<OUT> get() {
                if (collection == null)
                    throw new NullPointerException("Collection is null");
                else
                    return collection;
            }
        };
        return evaluate(operation);
    }

    @Override
    public <R> Stream<R> mapChecked(ThrowingFunction<? super OUT, ? extends R> mapper) {
        return new IntermediatePart<OUT, R>(this) {
            @Override
            public StreamOperation<OUT> getStreamOperation() {
                return new StreamOperation<OUT>() {
                    private StreamOperation<R> nextOperation = next.getStreamOperation();

                    @Override
                    public void start(StreamCharacteristics upstreamCharacteristics) {
                        if (mapper == null)
                            throw new NullPointerException("Mapper is null");
                        nextOperation.start(upstreamCharacteristics.withChecked(true));
                    }

                    @Override
                    public void acceptElement(StreamElement<OUT> element) {
                        if (element.type == StreamElementType.FAULTY)
                            nextOperation.acceptElement(element.tryAdapt());
                        else {
                            try {
                                StreamElement<R> mappedElement = StreamElement.of(mapper.apply(element.element));
                                nextOperation.acceptElement(mappedElement);
                            } catch (Exception e) {
                                nextOperation.acceptElement(element.withExceptionAdded(e));
                            }
                        }
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
        };
    }

    @Override
    public Stream<OUT> filterChecked(ThrowingPredicate<? super OUT> filter) {
        return new IntermediatePart<OUT, OUT>(this) {
            @Override
            public StreamOperation<OUT> getStreamOperation() {
                return new StreamOperation<OUT>() {
                    private StreamOperation<OUT> nextOperation = next.getStreamOperation();

                    @Override
                    public void start(StreamCharacteristics upstreamCharacteristics) {
                        nextOperation.start(upstreamCharacteristics.withChecked(true));
                    }

                    @Override
                    public void acceptElement(StreamElement<OUT> element) {
                        if (element.type == StreamElementType.FAULTY)
                            nextOperation.acceptElement(element);
                        else {
                            try {
                                if (filter.test(element.element))
                                    nextOperation.acceptElement(element);
                            } catch (Exception e) {
                                nextOperation.acceptElement(element.withExceptionAdded(e));
                            }
                        }
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
        };
    }

    @Override
    public Stream<OUT> onErrorMap(Function<? super List<Exception>, ? extends OUT> errorMapper) {
        return new IntermediatePart<OUT, OUT>(this) {
            @Override
            public StreamOperation<OUT> getStreamOperation() {
                return new StreamOperation<OUT>() {
                    private StreamOperation<OUT> nextOperation = next.getStreamOperation();

                    @Override
                    public void start(StreamCharacteristics upstreamCharacteristics) {
                        nextOperation.start(upstreamCharacteristics.withChecked(false));
                    }

                    @Override
                    public void acceptElement(StreamElement<OUT> element) {
                        if (element.type == StreamElementType.FAULTY) {
                            try {
                                StreamElement<OUT> newElement = StreamElement.of(errorMapper.apply(element.exceptions));
                                nextOperation.acceptElement(newElement);
                            } catch (Exception e) {
                                nextOperation.acceptElement(element.withExceptionAdded(e));
                            }
                        } else
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
        };
    }

    @Override
    public Stream<OUT> onErrorMapChecked(ThrowingFunction<? super List<Exception>, ? extends OUT> errorMapper) {
        return new IntermediatePart<OUT, OUT>(this) {
            @Override
            public StreamOperation<OUT> getStreamOperation() {
                return new StreamOperation<OUT>() {
                    private StreamOperation<OUT> nextOperation = next.getStreamOperation();

                    @Override
                    public void start(StreamCharacteristics upstreamCharacteristics) {
                        nextOperation.start(upstreamCharacteristics.withChecked(true));
                    }

                    @Override
                    public void acceptElement(StreamElement<OUT> element) {
                        if (element.type == StreamElementType.FAULTY) {
                            try {
                                StreamElement<OUT> newElement = StreamElement.of(errorMapper.apply(element.exceptions));
                                nextOperation.acceptElement(newElement);
                            } catch (Exception e) {
                                nextOperation.acceptElement(element.withExceptionAdded(e));
                            }
                        } else
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
        };
    }

    @Override
    public Stream<OUT> onErrorFilter() {
        return new IntermediatePart<OUT, OUT>(this) {

            @Override
            public StreamOperation<OUT> getStreamOperation() {
                return new StreamOperation<OUT>() {
                    private StreamOperation<OUT> nextOperation = next.getStreamOperation();

                    @Override
                    public void start(StreamCharacteristics upstreamCharacteristics) {
                        nextOperation.start(upstreamCharacteristics.withChecked(false));
                    }

                    @Override
                    public void acceptElement(StreamElement<OUT> element) {
                        if (element.type == StreamElementType.REGULAR)
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
        };
    }

}