package gad.abtree;

/**
 * Diese Klasse repräsentiert ein Blatt des Baumes.
 */
public class ABTreeLeaf extends ABTreeNode {

    public ABTreeLeaf(int a, int b) {
        super(a, b);
    }

    @Override
    public void insert(int key) {
    }

    @Override
    public boolean canSteal() {
        return false;
    }

    @Override
    public boolean find(int key) {
        return false;
    }

    @Override
    public boolean remove(int key) {
        return false;
    }

    @Override
    public int height() {
        return 0;
    }

    @Override
    public Integer min() {
        return null;
    }

    @Override
    public Integer max() {
        return null;
    }

    @Override
    public boolean validAB(boolean root) {
        return true;
    }

    @Override
    public int dot(StringBuilder sb, int from) {
        sb.append(String.format("\tstruct%d [label=leaf, shape=ellipse];\n", from));
        return from + 1;
    }

    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    public Integer steal(boolean head) {
        return null;
    }
}