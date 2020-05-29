package pgdp.lists;

public class GamesWithLists {
  public static IntDoubleList reuniteLists(IntDoubleList[] arrayOfLists) {
    IntDoubleList allLists = new IntDoubleList();
    for (IntDoubleList list : arrayOfLists)
      for (int i = 0; i < list.size(); i++)
        allLists.append(list.get(i));

    IntDoubleList result = new IntDoubleList();
    while (allLists.size() > 0) {
      int min = allLists.copy().min();
      for (int i = 0; i < allLists.size(); i++) {
        int num = allLists.get(i);
        if (num == min) {
          allLists.remove(i);
          i--;
          result.append(min);
        }
      }
    }

    return result;
  }

  public static IntDoubleList partTheList(IntDoubleList list, int value) {
    // Get all the nums smaller than value
    // Save their indecies
    IntDoubleList smallerNums = new IntDoubleList();
    IntDoubleList indecies = new IntDoubleList();
    for (int i = 0; i < list.size(); i++) {
      int num = list.get(i);
      if (num < value) {
        smallerNums.append(num);
        indecies.append(i);
      }
    }

    // Remove all elements that all smaller
    int removed = 0;
    for (int i = 0; i < indecies.size(); i++) {
      int index = indecies.get(i);
      list.remove(index - removed++);
    }

    // Add the rest of the nums to smaller nums
    for (int i = 0; i < list.size(); i++) {
      int num = list.get(i);
      smallerNums.append(num);
    }

    return smallerNums;
  }

  public static IntDoubleList mixedList(IntDoubleList list) {
    int end = list.size() - 1;

    IntDoubleList newList = new IntDoubleList();

    for (int i = 0; i <= end / 2; i++) {
      int a = list.get(i);
      int b = list.get(end - i);
      if (i == end - i)
        newList.append(a);
      else {
        newList.append(a);
        newList.append(b);
      }
    }

    return newList;
  }
}
