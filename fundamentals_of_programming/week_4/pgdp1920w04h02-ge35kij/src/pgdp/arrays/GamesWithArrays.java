package pgdp.arrays;

public class GamesWithArrays {
  // Done
  public static int[] otherSort(int[] arr, int[] arr2) {
    int[] nums = new int[arr.length];

    String outcasts = "";
    int nextIndex = 0;
    for (int el : arr2) {
      for (int i = 0; i < arr.length; i++) {
        if (arr[i] == el)
          nums[nextIndex++] = el;
        else {
          if (!contains(arr2, arr[i])) {
            outcasts += String.format("%d ", arr[i]);
          }
        }
      }
    }

    // Get rest
    String[] rest = outcasts.split(" ");
    int[] restInt = new int[rest.length];
    for (int i = 0; i < rest.length; i++) {
      restInt[i] = Integer.parseInt(rest[i]);
    }
    restInt = distinct(restInt);
    for (int i = 0; i < restInt.length; i++) {
      nums[nextIndex++] = restInt[i];
    }
    return nums;
  }

  // Done.
  public static int[] fairFriends(int[] arr, int[] arr2) {
    int[] res = new int[2];

    int sum1 = 0;
    int sum2 = 0;
    for (int el : arr)
      sum1 += el;
    for (int el : arr2)
      sum2 += el;
    int middle = (sum1 + sum2) / 2;

    boolean done = false;
    for (int i = 0; i < arr.length; i++) {
      for (int j = 0; j < arr2.length; j++) {
        int num1 = arr[i];
        int num2 = arr2[j];

        if (sum1 - num1 + num2 == middle && sum2 - num2 + num1 == middle) {
          res = new int[] { num1, num2 };
          done = true;
          break;
        }
      }

      if (done)
        break;
    }

    return res;
  }

  // Done.
  public static boolean alpen(int[] arr) {
    if (arr[0] > arr[1])
      return false;

    boolean hasReachedPeak = false;
    for (int i = 1; i < arr.length - 1; i++) {
      int current = arr[i];
      int next = arr[i + 1];

      if (current < next && !hasReachedPeak)
        continue;
      if (current > next && hasReachedPeak)
        continue;
      if (current > next && !hasReachedPeak)
        hasReachedPeak = true;
      if (current < next && hasReachedPeak)
        return false;
    }

    return true;
  }

  // Done.
  public static int[] plankton(int[] arr) {
    int[] res = new int[3];

    int maxDifference = 0;
    for (int i = 0; i < arr.length - 1; i++) {
      for (int j = i + 1; j < arr.length; j++) {
        int num1 = arr[i];
        int num2 = arr[j];

        int diff = num2 - num1;
        if (diff > maxDifference) {
          maxDifference = diff;
          res = new int[] { i, j, diff };
        }
      }
    }

    return res;
  }

  // Done.
  public static int pinguinFreunde(int[] arr) {
    if (arr.length % 2 != 0)
      return 0;

    int[] distinct = distinct(arr);
    int[] counts = new int[distinct.length];

    for (int index = 0; index < distinct.length; index++) {
      int count = 0;
      int el = distinct[index];
      for (int i = 0; i < arr.length; i++) {
        if (arr[i] == el)
          count++;
      }
      counts[index] = count;
    }

    // If counts are all divisible by min
    int min = min(counts);

    for (int i = 0; i < counts.length; i++) {
      if (counts[i] % min != 0)
        return 0;
    }

    return min;
  }

  // Helper methods
  private static boolean contains(int[] arr, int el) {
    for (int i = 0; i < arr.length; i++) {
      if (arr[i] == el)
        return true;
    }

    return false;
  }

  private static int[] distinct(int[] arr) {
    int[] distinctArr = new int[0];

    // Go thru all elements from arr
    for (int i = 0; i < arr.length; i++) {

      int el = arr[i];
      // if distinctArr does not contain el, add it
      if (!contains(distinctArr, el)) {
        var temp = new int[distinctArr.length + 1];
        temp[distinctArr.length] = el;
        for (int j = 0; j < distinctArr.length; j++) {
          temp[j] = distinctArr[j];
        }
        distinctArr = temp;
      }
    }

    return distinctArr;
  }

  private static int min(int[] arr) {
    int min = arr[0];
    for (int i = 1; i < arr.length; i++) {
      if (arr[i] < min) {
        min = arr[i];
      }
    }
    return min;
  }
}
