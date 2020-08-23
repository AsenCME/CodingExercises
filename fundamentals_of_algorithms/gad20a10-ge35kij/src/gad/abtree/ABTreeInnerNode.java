package gad.abtree;

import java.util.ArrayList;
import java.util.List;

public class ABTreeInnerNode extends ABTreeNode {
    private List<Integer> keys;
    private List<ABTreeNode> children;

    public ABTreeInnerNode(List<Integer> keys, List<ABTreeNode> children, int a, int b) {
        super(a, b);
        this.keys = keys;
        this.children = children;
    }

    public ABTreeInnerNode(int key, ABTreeNode left, ABTreeNode right, int a, int b) {
        super(a, b);
        keys = new ArrayList<>();
        children = new ArrayList<>();
        keys.add(key);
        children.add(left);
        children.add(right);
    }

    public ABTreeInnerNode(int key, int a, int b) {
        this(key, new ABTreeLeaf(a, b), new ABTreeLeaf(a, b), a, b);
    }

    public List<ABTreeNode> getChildren() {
        return children;
    }

    public List<Integer> getKeys() {
        return keys;
    }

    @Override
    public void insert(int key) {
        if (this.children.stream().allMatch(x -> x.height() == 0)) {
            this.keys.add(key);
            this.keys.sort(Integer::compare);
            this.children.add(new ABTreeLeaf(a, b));
        } else {

            int index = -1;
            while (++index < this.keys.size())
                if (this.keys.get(index) > key)
                    break;
            this.children.get(index).insert(key);
            this.checkForSplit(index);
        }
    }

    public void checkForSplit(int index) {
        var child = (ABTreeInnerNode) this.children.get(index);
        if (child.keys.size() >= this.b)
            this.splitChid(index, child);
    }

    public ABTreeInnerNode splitNode(ABTreeNode node) {
        if (node instanceof ABTreeLeaf || ((ABTreeInnerNode) node).children.size() <= this.b)
            return null;

        var innerNode = (ABTreeInnerNode) node;
        // Get Mid
        int mid = innerNode.keys.size() / 2;
        int midValue = innerNode.keys.get(mid);

        // Make Left
        var leftKeys = new ArrayList<Integer>();
        var leftChildren = new ArrayList<ABTreeNode>();
        leftKeys.addAll(innerNode.keys.subList(0, mid));
        leftChildren.addAll(innerNode.children.subList(0, mid + 1));
        var leftChild = new ABTreeInnerNode(leftKeys, leftChildren, a, b);

        // Make Right
        var rightKeys = new ArrayList<Integer>();
        var rightChildren = new ArrayList<ABTreeNode>();
        rightKeys.addAll(innerNode.keys.subList(mid + 1, this.b));
        rightChildren.addAll(innerNode.children.subList(mid + 1, this.b + 1));
        var rightChild = new ABTreeInnerNode(rightKeys, rightChildren, a, b);

        return new ABTreeInnerNode(midValue, leftChild, rightChild, a, b);
    }

    public void splitChid(int index, ABTreeInnerNode child) {
        this.children.remove(child);

        // Get Mid
        int mid = child.keys.size() / 2;
        int midValue = child.keys.get(mid);

        // Make Left
        var leftKeys = new ArrayList<Integer>();
        var leftChildren = new ArrayList<ABTreeNode>();
        leftKeys.addAll(child.keys.subList(0, mid));
        leftChildren.addAll(child.children.subList(0, mid + 1));
        var leftChild = new ABTreeInnerNode(leftKeys, leftChildren, a, b);

        // Make Right
        var rightKeys = new ArrayList<Integer>();
        var rightChildren = new ArrayList<ABTreeNode>();
        rightKeys.addAll(child.keys.subList(mid + 1, this.b));
        rightChildren.addAll(child.children.subList(mid + 1, this.b + 1));
        var rightChild = new ABTreeInnerNode(rightKeys, rightChildren, a, b);

        // Update tree
        this.keys.add(index, midValue);
        this.children.add(index, rightChild);
        this.children.add(index, leftChild);
    }

    @Override
    public boolean canSteal() {
        return this.children.size() > this.a;
    }

    public Integer steal(boolean head) {
        int index = head ? 0 : this.keys.size() - 1;
        if (this.isLeaf())
            return this.keys.get(index);
        else
            return this.children.get(head ? 0 : this.children.size() - 1).steal(head);
    }

    @Override
    public boolean find(int key) {
        if (this.keys.contains(key))
            return true;

        for (int i = 0; i < this.keys.size(); i++)
            if (this.keys.get(i) > key)
                return this.children.get(i).find(key);

        return this.children.get(this.children.size() - 1).find(key);
    }

    @Override
    public boolean remove(int key) {
        int index = this.keys.indexOf(key);
        if (index != -1) {
            if (this.isLeaf()) {
                this.keys.remove(index);
                this.children.remove(index + 1);
                return true;
            } else {
                if (this.children.get(index).canSteal()) {
                    int value = this.children.get(index).steal(false);
                    this.keys.set(index, value);
                    this.children.get(index).remove(value);
                } else if (this.children.get(index + 1).canSteal()) {
                    int value = this.children.get(index + 1).steal(true);
                    this.keys.set(index, value);
                    this.children.get(index + 1).remove(value);
                } else {
                    this.mergeOnIndex(index);
                    this.children.get(index).remove(key);
                }
            }
        } else {
            for (index = 0; index < this.keys.size(); index++)
                if (this.keys.get(index) > key)
                    break;

            var atEnd = this.children.size() - 1 == index;
            this.children.get(index).remove(key);

            var node = (ABTreeInnerNode) this.children.get(index);
            if (node.children.size() < this.a)
                if (atEnd && node.keys.size() == 0)
                    rebalanace(index - 1);
                else
                    rebalanace(index);
        }
        return true;
    }

    public void rebalanace(int index) {
        var child = (ABTreeInnerNode) this.children.get(index);
        if (index - 1 >= 0 && this.children.get(index - 1).canSteal()) {
            var sibling = (ABTreeInnerNode) this.children.get(index - 1);
            child.keys.add(0, this.keys.get(index - 1));
            this.keys.set(index - 1, sibling.keys.get(sibling.keys.size() - 1));
            sibling.keys.remove(sibling.keys.size() - 1);
            child.children.add(0, sibling.children.get(sibling.children.size() - 1));
            sibling.children.remove(sibling.children.size() - 1);
        } else if (index + 1 < this.children.size() - 1 && this.children.get(index + 1).canSteal()) {
            var sibling = (ABTreeInnerNode) this.children.get(index + 1);
            child.keys.add(this.keys.get(index));
            this.keys.set(index, sibling.keys.get(0));
            sibling.keys.remove(0);
            child.children.add(sibling.children.get(0));
            sibling.children.remove(0);
        } else {
            if (index == this.children.size() - 1)
                this.mergeOnIndex(index - 1);
            else
                this.mergeOnIndex(index);
        }
    }

    public void mergeOnIndex(int index) {
        var left = this.children.get(index);
        var right = this.children.get(index + 1);
        if (left instanceof ABTreeLeaf && right instanceof ABTreeLeaf)
            this.children.set(0, left);

        var leftNode = (ABTreeInnerNode) left;
        var rightNode = (ABTreeInnerNode) right;

        var newKeys = new ArrayList<Integer>();
        var newChildren = new ArrayList<ABTreeNode>();
        newKeys.addAll(leftNode.keys);
        newKeys.add(this.keys.get(index));
        this.keys.remove(index);
        newKeys.addAll(rightNode.keys);

        newChildren.addAll(leftNode.children);
        newChildren.addAll(rightNode.children);
        var newNode = new ABTreeInnerNode(newKeys, newChildren, a, b);

        if (newNode.isLeaf() && newNode.children.size() - 1 != newNode.keys.size())
            newNode.children.remove(0);

        this.children.set(index, newNode);
        this.children.remove(right);
    }

    @Override
    public boolean isLeaf() {
        return this.children.stream().allMatch(x -> x.height() == 0);
    }

    public ABTreeNode mergeNodes(ABTreeNode left, ABTreeNode right) {
        if (left instanceof ABTreeLeaf && right instanceof ABTreeLeaf)
            return left;

        var leftNode = (ABTreeInnerNode) left;
        var rightNode = (ABTreeInnerNode) right;

        var newKeys = new ArrayList<Integer>();
        var newChildren = new ArrayList<ABTreeNode>();
        newKeys.addAll(leftNode.keys);
        newKeys.addAll(rightNode.keys);

        var leftEndNode = leftNode.children.get(leftNode.children.size() - 1);
        var rightStartNode = rightNode.children.get(0);
        var middleNode = this.mergeNodes(leftEndNode, rightStartNode);
        leftNode.children.remove(leftNode.children.size() - 1);
        rightNode.children.remove(0);

        newChildren.addAll(leftNode.children);
        newChildren.add(middleNode);
        newChildren.addAll(rightNode.children);
        var newNode = new ABTreeInnerNode(newKeys, newChildren, a, b);
        if (newNode.isLeaf() && this.children.size() - 1 != this.keys.size())
            newNode.children.remove(0);
        return newNode;
    }

    @Override
    public int height() {
        return 1;
    }

    @Override
    public Integer min() {
        var opt = this.keys.stream().min(Integer::compare);
        if (opt.isPresent())
            return opt.get();
        else
            return null;
    }

    @Override
    public Integer max() {
        var opt = this.keys.stream().max(Integer::compare);
        if (opt.isPresent())
            return opt.get();
        else
            return null;
    }

    @Override
    public boolean validAB(boolean root) {
        // Check for equal height
        int height = this.children.get(0).height();
        for (ABTreeNode node : children)
            if (node.height() != height)
                return false;

        // Check if between a and b
        // skip for root
        if ((!root && this.children.size() < this.a) || this.children.size() > this.b)
            return false;

        // Check keys sorted
        for (int i = 1; i < this.keys.size(); i++)
            if (this.keys.get(i) < this.keys.get(i - 1))
                return false;

        // Check keys of children
        // Skip if node is leaf
        if (!this.children.stream().allMatch(x -> x.height() == 0)) {
            for (int i = 0; i < this.keys.size(); i++) {
                int key = this.keys.get(i);
                var leftChildMax = this.children.get(i).max();
                var rightChildMin = this.children.get(i + 1).min();
                if (leftChildMax != null && leftChildMax >= key)
                    return false;
                if (rightChildMin != null && rightChildMin <= key)
                    return false;
            }
        }

        return this.children.stream().allMatch(x -> x.validAB(false));
    }

    @Override
    public int dot(StringBuilder sb, int from) {
        int mine = from++;
        sb.append(String.format("\tstruct%s [label=\"", mine));
        for (int i = 0; i < 2 * keys.size() + 1; i++) {
            if (i > 0)
                sb.append("|");
            sb.append(String.format("<f%d> ", i));
            if (i % 2 == 1)
                sb.append(keys.get(i / 2));
        }
        sb.append("\"];\n");
        for (int i = 0; i < children.size(); i++) {
            int field = 2 * i;
            sb.append(String.format("\tstruct%d:<f%d> -> struct%d;\n", mine, field, from));
            from = children.get(i).dot(sb, from);
        }
        return from;
    }
}
