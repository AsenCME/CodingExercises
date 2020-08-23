package gad.avl;

import java.util.ArrayList;

public class AVLTreeNode {
    private int key;
    private int balance = 0;
    private AVLTreeNode left = null;
    private AVLTreeNode right = null;

    public AVLTreeNode(int key) {
        this.key = key;
    }

    public AVLTreeNode getLeft() {
        return left;
    }

    public AVLTreeNode getRight() {
        return right;
    }

    public int getBalance() {
        return balance;
    }

    public int getKey() {
        return key;
    }

    public void setLeft(AVLTreeNode left) {
        this.left = left;
    }

    public void setRight(AVLTreeNode right) {
        this.right = right;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    private void updateBalance() {
        int leftHeight = 0;
        int rightHeight = 0;
        if (this.left != null) {
            leftHeight = this.left.height();
            this.left.updateBalance();
        }
        if (this.right != null) {
            rightHeight = this.right.height();
            this.right.updateBalance();
        }

        this.balance = rightHeight - leftHeight;
    }

    public int height() {
        int leftHeight = 0;
        int rightHeight = 0;
        if (this.left != null)
            leftHeight = this.left.height();
        if (this.right != null)
            rightHeight = this.right.height();

        return 1 + Math.max(leftHeight, rightHeight);
    }

    private boolean isBalanced() {
        var leftHeight = 0;
        var rightHeight = 0;
        var leftBalanced = true;
        var rightBalanced = true;
        if (this.left != null) {
            leftHeight = this.left.height();
            leftBalanced = this.left.isBalanced();
        }
        if (this.right != null) {
            rightHeight = this.right.height();
            rightBalanced = this.right.isBalanced();
        }

        var currentBalance = rightHeight - leftHeight;
        return this.balance == currentBalance && Math.abs(currentBalance) <= 1 && leftBalanced && rightBalanced;
    }

    private boolean isSorted(int startKey) {
        var leftSorted = true;
        var rightSorted = true;
        if (this.left != null)
            leftSorted = this.left.key < this.key && this.left.isSorted(startKey);
        if (this.right != null)
            rightSorted = this.right.key >= this.key && this.right.isSorted(startKey);

        return leftSorted && rightSorted;
    }

    private boolean hasCycle(ArrayList<AVLTreeNode> visited) {
        if (visited.contains(this))
            return false;

        visited.add(this);
        var leftHasCycles = false;
        var rightHasCycles = false;
        if (this.left != null)
            leftHasCycles = this.left.hasCycle(visited);
        if (this.right != null)
            rightHasCycles = this.right.hasCycle(visited);
        return leftHasCycles || rightHasCycles;
    }

    public boolean validAVL() {
        if (left == null && right == null)
            return true;

        // Check if balanced
        if (!this.isBalanced())
            return false;

        // Check if all left are smaller
        if (!this.isSorted(this.key))
            return false;

        // Check if it has cycles
        // if (this.hasCycle(new ArrayList<AVLTreeNode>()))
        // return false;

        return true;
    }

    public boolean find(int key) {
        if (key == this.key)
            return true;

        if (key < this.key && this.left == null)
            return false;
        if (key > this.key && this.right == null)
            return false;

        if (key < this.key && this.left != null)
            return this.left.find(key);
        if (key > this.key && this.right != null)
            return this.right.find(key);

        return false;
    }

    public AVLTreeNode insert(int key) {
        if (key < this.key) {
            if (this.left == null)
                this.left = new AVLTreeNode(key);
            else
                this.left = this.left.insert(key);
        } else {
            if (this.right == null)
                this.right = new AVLTreeNode(key);
            else
                this.right = this.right.insert(key);
        }

        this.updateBalance();

        // balance tree
        if (!this.validAVL()) {
            if (this.balance == -2) {
                if (this.left.balance == 1)
                    return rotateLeftRight();
                else
                    return rotateRight();
            } else {
                if (this.right.balance == -1)
                    return rotateRightLeft();
                else
                    return rotateLeft();
            }
        } else
            return this;
    }

    private AVLTreeNode rotateRight() {
        var tempNode = this.left;
        this.left = tempNode.right;
        tempNode.right = this;
        tempNode.updateBalance();
        return tempNode;
    }

    private AVLTreeNode rotateLeft() {
        var tempNode = this.right;
        this.right = tempNode.left;
        tempNode.left = this;
        tempNode.updateBalance();
        return tempNode;
    }

    private AVLTreeNode rotateLeftRight() {
        this.left = this.left.rotateLeft();
        return this.rotateRight();
    }

    private AVLTreeNode rotateRightLeft() {
        this.right = this.right.rotateRight();
        return this.rotateLeft();
    }

    /**
     * Diese Methode wandelt den Baum in das Graphviz-Format um.
     *
     * @param sb der StringBuilder für die Ausgabe
     */
    public void dot(StringBuilder sb) {
        dotNode(sb, 0);
    }

    private int dotNode(StringBuilder sb, int idx) {
        sb.append(String.format("\t%d [label=\"%d, b=%d\"];\n", idx, key, balance));
        int next = idx + 1;
        if (left != null)
            next = left.dotLink(sb, idx, next, "l");
        if (right != null)
            next = right.dotLink(sb, idx, next, "r");
        return next;
    }

    private int dotLink(StringBuilder sb, int idx, int next, String label) {
        sb.append(String.format("\t%d -> %d [label=\"%s\"];\n", idx, next, label));
        return dotNode(sb, next);
    };
}
