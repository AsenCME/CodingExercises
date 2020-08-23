package gad.simplesort;

import java.util.Arrays;

public class StudentResult implements Result {
    @Override
    public void logPartialArray(int[] array) {
        System.out.println("Logging array " + Arrays.toString(array));
    }
}