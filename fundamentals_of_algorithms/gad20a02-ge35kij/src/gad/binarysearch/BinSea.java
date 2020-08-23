package gad.binarysearch;

import gad.binarysearch.Interval.EmptyInterval;
import gad.binarysearch.Interval.NonEmptyInterval;

public class BinSea {

        public static int search(int[] arr, int value, boolean lower, Result result) {
                if (arr.length == 0 || arr == null)
                        return -1;

                int start = 0, end = arr.length - 1, mid;
                while (start < end) {
                        mid = (start + end) / 2;
                        result.addStep(mid);

                        if (arr[mid] == value)
                                return mid;
                        if (arr[mid] < value)
                                start = mid + 1;
                        else
                                end = mid - 1;
                }

                mid = start;
                int current = arr[mid];
                if (current == value)
                        return mid;

                if (lower && current < value)
                        mid++;
                else if (!lower && current > value)
                        mid--;

                if (mid > arr.length - 1 || mid < 0)
                        return -1;
                else
                        return mid;
        }

        public static Interval search(int[] sortedData, Interval valueRange, Result resultLower, Result resultHigher) {
                if (valueRange instanceof EmptyInterval)
                        return new EmptyInterval();

                int lowerBound = search(sortedData, valueRange.getFrom(), true, resultLower);
                int higherBound = search(sortedData, valueRange.getTo(), false, resultHigher);

                return Interval.fromArrayIndices(lowerBound, higherBound);
        }

        public static void main(String[] args) {
                System.out.println(search(new int[] { 1 }, 100, true, new StudentResult()));
                System.out.println(
                                search(new int[] { 1, 27, 100, 127, 1000, 2000, 3000 }, 80, true, new StudentResult()));

                System.out.println(search(new int[] { 10, 20, 30, 40 }, new NonEmptyInterval(20, 50),
                                new StudentResult(), new StudentResult()));
                System.out.println(search(new int[] { 1, 27, 100, 127, 3000 }, new NonEmptyInterval(100, 10000),
                                new StudentResult(), new StudentResult()));
                System.out.println(search(new int[] { 1, 27, 100, 127, 3000 }, new NonEmptyInterval(0, 10000),
                                new StudentResult(), new StudentResult()));
                System.out.println(search(new int[] { 1, 27, 100, 127, 3000 }, new NonEmptyInterval(-10, 10000),
                                new StudentResult(), new StudentResult()));
        }
}
