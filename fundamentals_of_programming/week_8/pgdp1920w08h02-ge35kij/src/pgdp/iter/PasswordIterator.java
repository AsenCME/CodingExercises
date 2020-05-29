package pgdp.iter;

import java.util.Iterator;

public class PasswordIterator implements Iterator<String> {
    private final int passLen, maxCount;
    private int iteratorIndex = 0, type = 1;
    private int type1Max, type23Max;
    private int typeIndex = 0, type4Start;
    private String[] types;

    public PasswordIterator(int passwordLength) {
        if (passwordLength < 1 || passwordLength > 9)
            Util.badArgument("Password length must be between 1 and 9 inclusive.");
        passLen = passwordLength;
        maxCount = (int) Math.pow(10, passLen);

        type1Max = 9;
        type23Max = 10 - passLen;
        type4Start = 1;

        types = new String[32 - 2 * passLen];
    }

    public boolean hasNext() {
        return iteratorIndex < maxCount;
    }

    public String next() {
        if (iteratorIndex >= maxCount)
            Util.noSuchElement("There are no more passwords to generate");

        String value = "";

        switch (type) {
        case 1:
            value = getNextType1();
            if (typeIndex > type1Max) {
                type++;
                typeIndex = 0;
            }
            break;
        case 2:
            value = getNextType2();
            if (typeIndex > type23Max) {
                type++;
                typeIndex = 0;
            }
            break;
        case 3:
            value = getNextType3();
            if (typeIndex > type23Max) {
                type++;
                typeIndex = 0;
            }
            break;

        case 4:
            value = getNextType4();
            break;

        default:
            Util.noSuchElement("There are no more passwords to generate");
            break;
        }

        iteratorIndex++;
        return value;
    }

    private String getNextType1() {
        String res = "";
        for (int i = 0; i < passLen; i++)
            res += typeIndex;
        typeIndex++;
        types[iteratorIndex] = res;
        return res;
    }

    private String getNextType2() {
        String res = "";
        for (int i : new Range(typeIndex, typeIndex + passLen - 1))
            res += i;
        typeIndex++;
        types[iteratorIndex] = res;
        return res;
    }

    private String getNextType3() {
        String res = "";
        for (int i : new Range(passLen + typeIndex - 1, typeIndex))
            res += i;
        typeIndex++;
        types[iteratorIndex] = res;
        return res;
    }

    private String getNextType4() {
        String a = Util.longToStringWithLength(type4Start, passLen);
        while (contains(types, a)) {
            type4Start++;
            a = Util.longToStringWithLength(type4Start, passLen);
        }
        type4Start++;
        return a;
    }

    private boolean contains(String[] arr, String a) {
        for (String value : arr)
            if (value.equals(a))
                return true;
        return false;
    }

}
