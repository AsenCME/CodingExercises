package pgdp.strings;

public class Main {
    public static void main(String[] args) {
        MyString str1 = new MyString(new char[] { 'a', 'b' });
        str1.concat(new char[] { 'c', 'd', 'e' });
        str1.concat(new char[] { 'f' });

        MyString str2 = new MyString(new char[] { 'a', 'b', 'c', 'd' });
        str2.concat(new char[] { 'e', 'f' });

        boolean res = str1.equals(str2); // true
    }
}