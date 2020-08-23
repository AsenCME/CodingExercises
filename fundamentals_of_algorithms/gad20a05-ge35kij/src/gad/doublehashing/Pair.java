package gad.doublehashing;

public class Pair<One, Two> {
    private final One one;
    private final Two two;

    public Pair(One one, Two two) {
        this.one = one;
        this.two = two;
    }

    public One getOne() {
        return one;
    }

    public Two getTwo() {
        return two;
    }

    public String toString() {
        return "(" + one + "," + two + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Pair) {
            Pair<?, ?> other = (Pair<?, ?>) o;
            return one.equals(other.one) && two.equals(other.two);
        } else {
            return false;
        }
    }
}
