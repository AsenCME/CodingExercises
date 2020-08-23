package gad.dynamicarray;

import java.util.Arrays;

public class DynamicArray {
    private int[] elements;
    private final int growthFactor;
    private final int maxOverhead;

    public int getInnerLength() {
        return elements.length;
    }

    /**
     * Dieser Konstruktor initialisiert ein dynamishes Feld. Es muss dabei gelten,
     * dass
     * 
     * 1. growthFactor >= 1 2. maxOverhead >= 2 3. growthFactor < maxOverhead
     * 
     * @param growthFactor der Wachstumsfaktor; um diesen wird der interne Speicher
     *                     vergrößtert, wenn nicht mehr genug Platz zur Verfügung
     *                     steht.
     * @param maxOverhead  der maximale Overhead; wird weniger als [maximaler
     *                     Overhead]-fache des genutzten Speichers benötigt, so wird
     *                     der interne Speicher verkleinert.
     */
    public DynamicArray(int growthFactor, int maxOverhead) {
        if (growthFactor < 1 || maxOverhead < 2 || maxOverhead <= growthFactor) {
            throw new IllegalArgumentException("DynamicArray(): Invalid arguments");
        }

        this.growthFactor = growthFactor;
        this.maxOverhead = maxOverhead;
        elements = new int[0];
    }

    public Interval reportUsage(Interval usage, int minSize) {
        // Determine size
        int size = usage.getSize(this.elements.length);
        if (minSize > size)
            size = minSize;

        if (size > this.elements.length || size * maxOverhead < this.elements.length) {
            // Resize
            size *= growthFactor;

            // Copy to new array and set elements
            int[] newArr = new int[size];
            if (!usage.isEmpty())
                newArr = this.copy(usage, newArr);
            this.elements = newArr;

            if (this.elements.length == 0 || usage.isEmpty())
                return new EmptyInterval();
            return new NonEmptyInterval(0, this.elements.length - 1);
        }

        return usage;
    }

    public int get(int index) {
        if (isOutOfRange(index))
            return -1;
        return this.elements[index];
    }

    public void set(int index, int value) {
        if (!isOutOfRange(index))
            this.elements[index] = value;
    }

    private boolean isOutOfRange(int index) {
        return index < 0 || index > this.elements.length - 1 || this.elements.length == 0;
    }

    private int[] copy(Interval interval, int[] newArr) {
        int start = interval.getFrom(), end = interval.getTo();

        if (start <= end) {
            for (int i = start; i <= end; i++)
                if (!isOutOfRange(i))
                    newArr[i - start] = this.elements[i];
        } else {
            int counter = interval.getSize(this.elements.length);
            int position = start;
            int index = 0;
            while (counter > 0) {
                newArr[index] = this.elements[position];
                position++;
                position %= this.elements.length;
                index++;
                counter--;
            }
        }

        return newArr;
    }

    /**
     * @return the elements
     */
    public int[] getElements() {
        return elements;
    }

    /**
     * @return the growthFactor
     */
    public int getGrowthFactor() {
        return growthFactor;
    }

    @Override
    public String toString() {
        return Arrays.toString(elements);
    }
}
