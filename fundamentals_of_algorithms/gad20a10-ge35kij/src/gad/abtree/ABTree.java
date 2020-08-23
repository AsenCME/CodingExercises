package gad.abtree;

import java.util.ArrayList;

public class ABTree {

    private final int a;
    private final int b;
    private int height = 0;

    private ABTreeInnerNode root;

    public ABTree(int a, int b) {
        this.a = a;
        this.b = b;
        this.root = null;
    }

    public ABTreeInnerNode getRoot() {
        return root;
    }

    public void setRoot(ABTreeInnerNode root) {
        this.root = root;
    }

    public boolean validAB() {
        return this.root.validAB(true);
    }

    public int height() {
        return this.height;
    }

    public boolean find(int key) {
        if (this.root == null)
            return false;
        else
            return this.root.find(key);
    }

    public void insert(int key) {
        if (this.root == null) {
            this.root = new ABTreeInnerNode(key, new ABTreeLeaf(a, b), new ABTreeLeaf(a, b), a, b);
            this.height = 1;
            return;
        }

        if (!this.find(key))
            this.root.insert(key);
        this.checkOverflow();
    }

    public void checkOverflow() {
        var keys = this.root.getKeys();
        var children = this.root.getChildren();
        if (keys.size() >= this.b) {
            int mid = this.b / 2;
            int midVal = keys.get(mid);

            var leftKeys = new ArrayList<Integer>();
            var leftChildren = new ArrayList<ABTreeNode>();
            leftKeys.addAll(keys.subList(0, mid));
            leftChildren.addAll(children.subList(0, mid + 1));
            var leftChild = new ABTreeInnerNode(leftKeys, leftChildren, a, b);

            var rightKeys = new ArrayList<Integer>();
            var rightChildren = new ArrayList<ABTreeNode>();
            rightKeys.addAll(keys.subList(mid + 1, this.b));
            rightChildren.addAll(children.subList(mid + 1, this.b + 1));
            var rightChild = new ABTreeInnerNode(rightKeys, rightChildren, a, b);
            this.root = new ABTreeInnerNode(midVal, leftChild, rightChild, a, b);
            this.height++;
        }
    }

    public boolean remove(int key) {
        if (!this.find(key))
            return false;

        if (this.root.isLeaf() && this.root.getKeys().size() == 1) {
            this.root = null;
            this.height = 0;
            return true;
        }

        boolean hasRemoved = this.root.remove(key);
        if (this.root.getKeys().size() == 0 && this.root.getChildren().size() == 1
                && this.root.getChildren().get(0).validAB(true))
            this.root = (ABTreeInnerNode) this.root.getChildren().get(0);
        return hasRemoved;
    }

    /**
     * Diese Methode wandelt den Baum in das Graphviz-Format um.
     *
     * @return der Baum im Graphiz-Format
     */
    public String dot() {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph {\n");
        sb.append("\tnode [shape=record];\n");
        if (root != null)
            root.dot(sb, 0);
        sb.append("}");
        return sb.toString();
    }

    @Override
    public String toString() {
        return dot();
    }

    public static void main(String[] args) {
        var tree = new ABTree(2, 4);

        tree.insert(109);
        tree.insert(23);
        tree.insert(49);
        tree.insert(180);
        tree.insert(120);
        tree.insert(163);
        tree.insert(172);
        tree.insert(130);
        tree.insert(95);
        tree.insert(156);
        tree.insert(99);
        tree.insert(39);
        tree.insert(178);
        tree.insert(197);
        tree.insert(71);
        tree.insert(194);
        tree.insert(118);
        tree.insert(88);

        tree.remove(156);
        tree.remove(99);
        tree.remove(39);
        tree.remove(71);
        tree.remove(130);
        tree.remove(120);
        tree.remove(118);
        tree.remove(178);
        tree.remove(49);
        tree.remove(163);
        tree.remove(109);
        tree.remove(172);
        tree.remove(194);
        tree.remove(197);
        tree.remove(180);
        tree.remove(88);
        tree.remove(95);
        tree.remove(23);
        System.out.println(tree.dot());
    }
}
