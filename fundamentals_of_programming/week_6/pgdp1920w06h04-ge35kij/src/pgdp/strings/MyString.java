package pgdp.strings;

class MyString {
    private char[] data;
    private MyString next;

    public MyString(char[] _data) {
        data = _data;
        next = null;
    }

    public int length() {
        if (next == null)
            return data.length;

        return data.length + next.length();
    }

    public void concat(char[] otherData) {
        // First added element
        if (next == null) {
            next = new MyString(otherData);
            return;
        }

        // Els - add to end
        MyString tail = next;
        while (tail.next != null)
            tail = tail.next;
        tail.concat(otherData);
    }

    public String toString() {
        char[] total = data;
        MyString tail = next;

        while (tail != null) {
            int len = total.length;
            int len2 = tail.data.length;
            char[] newData = new char[total.length + tail.data.length];
            for (int i = 0; i < len; i++)
                newData[i] = total[i];

            for (int i = 0; i < len2; i++)
                newData[i + len] = tail.data[i];
            total = newData;
            tail = tail.next;
        }

        return new String(total);
    }

    public boolean equals(MyString other) {
        if (other == null)
            return false;
        MyString a = this;
        MyString b = other;

        if (a.length() != b.length())
            return false;

        int shiftA = 0;
        int shiftB = 0;
        while (true) {
            if (!areArraysEqual(a.data, shiftA, b.data, shiftB))
                return false;

            if (a.next == null && b.next != null) {
                shiftA = b.data.length - shiftB;
                b = b.next;
                shiftB = 0;
            } else if (a.next != null && b.next == null) {
                shiftB = a.data.length - shiftA;
                a = a.next;
                shiftA = 0;
            } else if (a.next != null && b.next != null) {
                if (a.data.length - shiftA > b.data.length - shiftB) {
                    shiftA = b.data.length - shiftB;
                    b = b.next;
                    shiftB = 0;
                }

                else if (b.data.length - shiftB > a.data.length - shiftA) {
                    shiftB = a.data.length - shiftA;
                    a = a.next;
                    shiftA = 0;
                }
                // a == b

                else {
                    shiftA = 0;
                    shiftB = 0;
                    a = a.next;
                    b = b.next;
                }

            } else
                return true;
        }
        // return true;
    }

    private boolean areArraysEqual(char[] a, int shiftA, char[] b, int shiftB) {
        int len = Math.min(a.length - shiftA, b.length - shiftB);
        for (int i = 0; i < len; i++)
            if (a[i + shiftA] != b[i + shiftB])
                return false;

        return true;
    }

    public int indexOf(char c) {
        MyString a = this;
        int index = 0;
        while (a != null) {
            for (int i = 0; i < a.data.length; i++) {
                if (a.data[i] == c) {
                    return index + i;
                }
            }
            index += a.data.length;
            a = a.next;
        }
        return -1;
    }

    public int lastIndexOf(char c) {
        MyString a = this;
        int index = 0;
        int ourIndex = -1;
        while (a != null) {
            for (int i = 0; i < a.data.length; i++) {
                if (a.data[i] == c)
                    ourIndex = index + i;
            }
            index += a.data.length;
            a = a.next;
        }
        return ourIndex;
    }

}