package gad.dynamicarray;

public class DynamicStack {
    private DynamicArray dynArr;
    private int length;

    public int getLength() {
        return length;
    }

    public DynamicStack(int growthFactor, int maxOverhead) {
        dynArr = new DynamicArray(growthFactor, maxOverhead);
        length = 0;
    }

    public void pushBack(int value, Result result) {
        Interval interval = new EmptyInterval();
        if (length != 0)
            interval = new NonEmptyInterval(0, this.length - 1);
        int minSize = this.length + 1;

        dynArr.reportUsage(interval, minSize);
        dynArr.set(this.length, value);
        this.length++;

        if (result != null)
            result.logArray(dynArr.getElements());
    }

    public int popBack(Result result) {
        if (this.length == 0)
            return -1;
        this.length--;

        int element = dynArr.get(this.length);

        Interval interval = new EmptyInterval();
        if (length != 0)
            interval = new NonEmptyInterval(0, this.length - 1);
        int minSize = this.length;

        dynArr.reportUsage(interval, minSize);

        if (result != null)
            result.logArray(dynArr.getElements());

        return element;
    }

    protected void logStep(Result res) {
        res.logArray(this.dynArr.getElements());
    }

    @Override
    public String toString() {
        return dynArr + ", length: " + length;
    }
}