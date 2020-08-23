package gad.binomilia;

import java.util.List;

public interface Result {
    void startInsert(int value, List<BinomialTreeNode> heap);

    void startInsert(int value, BinomialTreeNode[] heap);

    void startDeleteMin(List<BinomialTreeNode> heap);

    void startDeleteMin(BinomialTreeNode[] heap);

    void logIntermediateStep(List<BinomialTreeNode> heap);

    void logIntermediateStep(BinomialTreeNode[] heap);

    void addToIntermediateStep(List<BinomialTreeNode> heap);

    void addToIntermediateStep(BinomialTreeNode[] heap);

    void addToIntermediateStep(BinomialTreeNode heap);
}
