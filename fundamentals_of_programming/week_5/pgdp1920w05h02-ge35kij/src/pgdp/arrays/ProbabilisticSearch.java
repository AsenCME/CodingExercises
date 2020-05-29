package pgdp.arrays;

public class ProbabilisticSearch extends MiniJava {
    /**
     * Binary Search aus der Vorlesung leicht abgewandelt
     */
    public static int[] find(int[] a, int x) {
        return find0(a, x, 0, a.length - 1, 1);
    }

    public static int[] find0(int[] a, int x, int n1, int n2, int numberOfSteps) {
        int t = (n1 + n2) / 2;
        if (a[t] == x)
            return new int[] { t, numberOfSteps };
        else if (n1 >= n2)
            return new int[] { -1, numberOfSteps };
        else if (x > a[t])
            return find0(a, x, t + 1, n2, numberOfSteps + 1);
        else if (n1 < t)
            return find0(a, x, n1, t - 1, numberOfSteps + 1);
        else
            return new int[] { -1, numberOfSteps };
    }

    public static int[] probalisticSearch(int[] arr, int value) {
        int min = min(arr);
        int max = max(arr);

        if (value > max || value < min)
            return new int[] { -1, 1 };

        float a = value - min;
        float b = max - min;
        float c = arr.length - 1;
        int index = Math.round(a / (b / c));
        if (index == arr.length)
            index = arr.length - 1;

        int valueAtPos = arr[index];
        if (valueAtPos == value)
            return new int[] { index, 1 };

        int jump = 1;
        int steps = 1;
        int direction = -1;
        int prevIndex = -1;
        if (valueAtPos > value) {
            while (valueAtPos > value) {
                prevIndex = index;
                index -= jump;
                jump *= 2;
                steps++;

                if (index < 0)
                    index = 0;
                valueAtPos = arr[index];
            }
        } else {
            direction = 1;
            while (valueAtPos < value) {
                prevIndex = index;
                index += jump;
                jump *= 2;
                steps++;

                if (index >= arr.length)
                    index = arr.length - 1;
                valueAtPos = arr[index];
            }
        }

        if (valueAtPos == value)
            return new int[] { index, steps };

        int[] res;
        if (direction == 1)
            res = find0(arr, value, prevIndex, index, steps);
        else
            res = find0(arr, value, index, prevIndex, steps);

        if (res[0] == -1)
            res[1]--;
        return res;
    }

    public static void compareApproaches(int[] arr, int min, int max) {
        var binary = find(arr, min);
        long binaryMaxSteps = binary[1];
        long binaryMaxValue = min;
        long binaryTotalSteps = binary[1];

        var prob = probalisticSearch(arr, min);
        long probMaxSteps = prob[1];

        long probMaxValue = min;
        long probTotalSteps = prob[1];

        for (int i = min + 1; i <= max; i++) {
            binary = find(arr, i);
            binaryTotalSteps += binary[1];
            if (binaryMaxSteps < binary[1]) {
                binaryMaxSteps = binary[1];
                binaryMaxValue = i;
            }

            prob = probalisticSearch(arr, i);
            probTotalSteps += prob[1];
            if (probMaxSteps < prob[1]) {
                probMaxSteps = prob[1];
                probMaxValue = i;
            }
        }

        write("Binäre Suche:\r\nMaximale Anzahl an Aufrufen:");
        write(binaryMaxSteps);
        write("Wert bei dem die maximale Anzahl an Aufrufen auftritt:");
        write(binaryMaxValue);
        write("Anzahl der gesamten Aufrufe:");
        write(binaryTotalSteps);

        write("Probabilistische Suche:\r\nMaximale Anzahl an Aufrufen:");
        write(probMaxSteps);
        write("Wert bei dem die maximale Anzahl an Aufrufen auftritt:");
        write(probMaxValue);
        write("Anzahl der gesamten Aufrufe:");
        write(probTotalSteps);
    }

    public static void main(String[] args) {
        int[] arr = new int[] { 6, 20, 22, 35, 51, 54, 59, 74, 77, 80, 87, 94, 97 };
        compareApproaches(arr, 34, 34);
    }

    private static int min(int[] a) {
        int min = a[0];
        for (int i = 1; i < a.length; i++)
            if (a[i] < min)
                min = a[i];
        return min;
    }

    private static int max(int[] a) {
        int max = a[0];
        for (int i = 1; i < a.length; i++)
            if (a[i] > max)
                max = a[i];
        return max;
    }
}
