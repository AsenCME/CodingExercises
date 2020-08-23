package gad.binomilia;

public class BinomialTreeNode {
    private int key;
    private BinomialTreeNode[] children;

    public BinomialTreeNode(int element) {
        this.key = element;
        children = new BinomialTreeNode[0];
    }

    public int min() {
        return this.key;
    }

    public int rank() {
        return children.length;
    }

    public BinomialTreeNode getChildWithRank(int rank) {
        return children[rank];
    }

    public BinomialTreeNode[] getChildren() {
        return children;
    }

    /*
     * public ... deleteMin() { // TODO return null; }
     */

    public static BinomialTreeNode merge(BinomialTreeNode a, BinomialTreeNode b) {
        var rank = a.rank();
        var temp = new BinomialTreeNode[rank + 1];
        var workingTree = a;
        var otherTree = b;
        if (b.key < a.key) {
            workingTree = b;
            otherTree = a;
        }
        for (int i = 0; i < rank; i++)
            temp[i] = workingTree.getChildWithRank(i);
        temp[rank] = otherTree;
        workingTree.children = temp;
        return workingTree;
    }
}