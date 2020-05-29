package pgdp.stream;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

enum StreamElementType {
    REGULAR, FAULTY
}

final class StreamElement<T> {
    final T element;
    final List<Exception> exceptions;
    final StreamElementType type;

    StreamElement() {
        element = null;
        exceptions = new ArrayList<Exception>();
        type = StreamElementType.REGULAR;
    }

    StreamElement(T _element) {
        element = _element;
        exceptions = new ArrayList<Exception>();
        type = StreamElementType.REGULAR;
    }

    StreamElement(T _element, List<Exception> _exceptions) {
        exceptions = _exceptions;

        if (!exceptions.isEmpty()) {
            type = StreamElementType.FAULTY;
            element = null;
        } else {
            type = StreamElementType.REGULAR;
            element = _element;
        }
    }

    T getElement() {
        return element;
    }

    List<Exception> getExceptions() {
        return exceptions;
    }

    boolean hasExceptions() {
        return !exceptions.isEmpty();
    }

    <R> StreamElement<R> withExceptionAdded(Exception ex) {
        // Since we have an Exception, newElement will definitely be null
        R newElement = (R) null;
        List<Exception> newExceptions = new ArrayList<Exception>();
        Collections.copy(newExceptions, exceptions);
        newExceptions.add(ex);
        return new StreamElement<R>(newElement, newExceptions);
    }

    <R> StreamElement<R> tryAdapt() {
        if (type == StreamElementType.REGULAR)
            throw new UnsupportedOperationException("Cannot adapt a regular StreamElement");
        else
            return new StreamElement<R>((R) null, List.copyOf(exceptions));
    }

    static <T> StreamElement<T> of(T t) {
        return new StreamElement<T>(t);
    }

    @Override
    public int hashCode() {
        return element.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        try {
            return element.equals(((StreamElement<?>) obj).element);
        } catch (ClassCastException e) {
            throw new ClassCastException("StreamElement can only be compared to StreamElement!");
        }
    }

    @Override
    public String toString() {
        return String.format("Element: %s | Exceptions: %s", element.toString(),
                exceptions.stream().map(Exception::toString).reduce("", (total, next) -> total += ", " + next));
    }
}