package gad.dynamicarray;

public class RingQueue {
    private DynamicArray dynArr;
    private int size;
    private int from;
    private int to;

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Dieser Konstruktor erzeugt eine neue Ringschlange. Ein leere Ringschlange
     * habe stets eine Größe von 0, sowie auf 0 gesetzte Objektvariablen to und
     * from.
     * 
     * @param growthFactor der Wachstumsfaktor des zugrundeliegenden dynamischen
     *                     Feldes
     * @param maxOverhead  der maximale Overhead des zugrundeliegenden dynamischen
     *                     Feldes
     */
    public RingQueue(int growthFactor, int maxOverhead) {
        dynArr = new DynamicArray(growthFactor, maxOverhead);
        size = 0;
        from = 0;
        to = 0;
    }

    public void enqueue(int value, Result result) {
        Interval interval = new EmptyInterval();
        if (this.size != 0)
            interval = new NonEmptyInterval(this.from, this.to);
        this.size++;
        int minSize = this.size;

        Interval usage = this.dynArr.reportUsage(interval, minSize);

        if (!interval.isEmpty() && interval.getFrom() > interval.getTo()) {
            int size = (interval.getSize(this.dynArr.getInnerLength()) - 1) / this.dynArr.getGrowthFactor();
            this.to += size - 1;
            this.from = 0;
        }

        if (!usage.isEmpty())
            this.to++;
        if (this.to >= this.dynArr.getInnerLength())
            this.to = 0;
        this.dynArr.set(this.to, value);

        result.logArray(this.dynArr.getElements());
    }

    public int dequeue(Result result) {
        int value = dynArr.get(this.from);
        this.size--;
        if (this.size < 0)
            this.size = 0;
        this.from++;
        if (this.from >= this.dynArr.getInnerLength())
            this.from = 0;

        int minSize = size;
        Interval interval = new EmptyInterval();
        if (this.size != 0)
            interval = new NonEmptyInterval(this.from, this.to);

        Interval usage = this.dynArr.reportUsage(interval, minSize);
        if (!usage.isEmpty() && !usage.equals(interval)) {
            int shift = this.from - usage.getFrom();
            this.from = usage.getFrom();
            this.to -= shift;
        }

        if (this.size == 0)
            this.from = this.to = 0;

        result.logArray(dynArr.getElements());
        return value;
    }

    @Override
    public String toString() {
        return dynArr + ", size: " + size + ", from " + from + " to " + to;
    }
}