package pgdp.tree;

import java.util.List;

public interface Segmentable<T> {
    /**
     * Segments must be strongly typed and not changable
     * 
     * @return non-null list of segments of the same generic type
     */
    List<T> getSegments();
}
