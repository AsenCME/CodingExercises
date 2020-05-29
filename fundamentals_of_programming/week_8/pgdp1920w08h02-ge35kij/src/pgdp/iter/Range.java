package pgdp.iter;

import java.util.Iterator;

class Range implements Iterable<Integer> {
    private final int begin, end, stride;

    public Range(int _begin, int _end, int _stride) {
        if (_stride <= 0)
            Util.badArgument("Stride must be greater than 0.");
        begin = _begin;
        end = _end;
        stride = begin < end ? _stride : -_stride;
    }

    public Range(int _begin, int _end) {
        begin = _begin;
        end = _end;

        stride = begin <= end ? 1 : -1;
    }

    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {
            int current = begin - stride;

            @Override
            public boolean hasNext() {
                if (begin > end)
                    return current + stride >= end;
                return current + stride <= end;
            }

            @Override
            public Integer next() {
                current += stride;
                if (begin < end && current > end)
                    Util.noSuchElement("Element out of range.");
                if (begin > end && current < end)
                    Util.noSuchElement("Element out of range");
                return current;
            }
        };
    }
}