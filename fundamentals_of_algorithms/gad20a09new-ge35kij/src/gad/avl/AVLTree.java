package gad.avl;

public class AVLTree {
    private AVLTreeNode root = null;

    public AVLTree() {
    }

    public AVLTreeNode getRoot() {
        return root;
    }

    public void setRoot(AVLTreeNode root) {
        this.root = root;
    }

    public boolean validAVL() {
        return this.root.validAVL();
    }

    public void insert(int key) {
        if (this.root == null)
            this.root = new AVLTreeNode(key);
        else
            this.root = this.root.insert(key);
    }

    public boolean find(int key) {
        return this.root.find(key);
    }

    /**
     * Diese Methode wandelt den Baum in das Graphviz-Format um.
     *
     * @return der Baum im Graphiz-Format
     */
    private String dot() {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph {\n");
        if (root != null)
            root.dot(sb);
        sb.append("}");
        return sb.toString();
    }

    @Override
    public String toString() {
        return dot();
    }

    public static void main(String[] args) {
        var tree = new AVLTree();
        tree.insert(14);
        tree.insert(11);
        tree.insert(17);
        tree.insert(7);
        tree.insert(53);
        tree.insert(4);
        tree.insert(13);
        tree.insert(12);
        tree.insert(8);
        tree.insert(60);
        tree.insert(19);
        tree.insert(16);
        tree.insert(20);
        System.out.println(tree.dot());
    }
}