package gad.binomilia;

import java.util.ArrayList;
import java.util.Arrays;

public class BinomialHeap {
    private BinomialTreeNode minNode;
    private ArrayList<BinomialTreeNode> heap;

    public BinomialHeap() {
        heap = new ArrayList<>();
        minNode = null;
    }

    public int min() {
        if (heap.size() == 0)
            throw new IllegalStateException("Empty heap does not have a min element");

        int minEl = heap.stream().mapToInt(x -> x.min()).min().getAsInt();
        minNode = heap.stream().filter(x -> x.min() == minEl).findFirst().get();
        return minEl;
    }

    public void insert(int key, Result result) {
        result.startInsert(key, heap);

        var newNode = new BinomialTreeNode(key);
        heap.add(newNode);
        result.logIntermediateStep(heap);

        if (heap.size() > 1)
            merge(newNode, result);
    }

    private void merge(BinomialTreeNode node, Result result) {
        var other = getSameRank(node);
        while (other != null) {
            heap.remove(other);
            heap.remove(node);
            node = BinomialTreeNode.merge(node, other);

            heap.add(node);
            result.logIntermediateStep(heap);
            other = getSameRank(node);
        }
    };

    private BinomialTreeNode getSameRank(BinomialTreeNode node) {
        int rank = node.rank();
        var other = heap.stream().filter(x -> x.rank() == rank && x != node).findFirst();
        if (other.isPresent())
            return other.get();
        return null;
    }

    public int deleteMin(Result result) {
        if (heap.size() == 0)
            throw new IllegalStateException("Cannot remove from empty heap.");

        result.startDeleteMin(heap);
        int minEl = min();
        var children = minNode.getChildren();
        heap.remove(minNode);

        heap.addAll(Arrays.asList(children));
        result.logIntermediateStep(heap);

        for (var child : children)
            merge(child, result);

        return minEl;
    }

    public static void main(String[] args) {
        var heap = new BinomialHeap();
        var result = new StudentResult();
        heap.insert(1, result);
        heap.insert(2, result);
        heap.insert(0, result);
        heap.insert(1, result);
        heap.deleteMin(result);
        heap.deleteMin(result);
        heap.deleteMin(result);
        heap.deleteMin(result);
        heap.deleteMin(result);
        heap.deleteMin(result);
        heap.deleteMin(result);
    }
}