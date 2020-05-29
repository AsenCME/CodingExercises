package gad.dynamicarray;

public class StackyQueue {
    private DynamicStack first;
    private DynamicStack second;

    public int getLength() {
        return this.first.getLength() + this.second.getLength();
    }

    public StackyQueue(int growthFactor, int maxOverhead) {
        this.first = new DynamicStack(growthFactor, maxOverhead);
        this.second = new DynamicStack(growthFactor, maxOverhead);
    }

    public void enqueue(int value, Result result1, Result result2) {
        first.pushBack(value, result1);
        this.second.logStep(result2);
    }

    public int dequeue(Result result1, Result result2) {
        int value = 0;
        if (this.second.getLength() == 0) {
            while (this.first.getLength() > 0)
                this.second.pushBack(this.first.popBack(result1), result2);
            value = this.second.popBack(result2);
        } else
            value = this.second.popBack(result2);

        return value;
    }

    @Override
    public String toString() {
        return first + ", " + second;
    }

    public static void main(String[] args) {
        var res1 = new StudentResult();
        var res2 = new StudentResult();
        StackyQueue queue = new StackyQueue(4, 8); // [], length: 0, [], length: 0
        queue.enqueue(4, res1, res2); // [4, 0, 0, 0], length: 1, [], length: 0
        queue.enqueue(1, res1, res2); // [4, 1, 0, 0], length: 2, [], length: 0
        queue.enqueue(5, res1, res2); // [4, 1, 5, 0], length: 3, [], length: 0
        queue.enqueue(6, res1, res2); // [4, 1, 5, 6], length: 4, [], length: 0
        queue.dequeue(res1, res2); // [], length: 0, [6, 5, 1, 4], length: 3
        queue.dequeue(res1, res2); // [], length: 0, [6, 5, 1, 4], length: 2
        queue.enqueue(2, res1, res2); // [2, 0, 0, 0], length: 1, [6, 5, 1, 4], length: 2
        queue.dequeue(res1, res2); // [2, 0, 0, 0], length: 1, [6, 5, 1, 4], length: 1
        queue.dequeue(res1, res2);
        queue.dequeue(res1, res2);
        queue.dequeue(res1, res2);
    }
}