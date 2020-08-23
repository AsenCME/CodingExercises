package gad.abtree;

import java.util.ArrayList;
import java.util.List;

public class Helper {
    public static List CreateLeafChildren(int count, int a, int b) {
        var list = new ArrayList<ABTreeLeaf>();
        for (int i = 0; i <= count; i++)
            list.add(new ABTreeLeaf(a, b));
        return list;
    }
}