package gad.dynamicarray;

import java.util.Arrays;

public class StudentResult implements Result {

    @Override
    public void logArray(int[] array) {
        System.out.println("Addad array " + Arrays.toString(array));
    }
}