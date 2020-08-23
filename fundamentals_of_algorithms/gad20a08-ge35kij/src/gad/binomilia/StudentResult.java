package gad.binomilia;

import java.util.List;

public class StudentResult implements Result {

    @Override
    public void startInsert(int value, List<BinomialTreeNode> heap) {
        System.out.println("Starte Einfügen mit Heap mit " + heap.size() + " Bäumen");
    }

    @Override
    public void startInsert(int value, BinomialTreeNode[] heap) {
        System.out.println("Starte Einfügen mit Heap mit " + heap.length + " Bäumen");
    }

    @Override
    public void startDeleteMin(List<BinomialTreeNode> heap) {
        System.out.println("Starte Löschen mit Heap mit " + heap.size() + " Bäumen");
    }

    @Override
    public void startDeleteMin(BinomialTreeNode[] heap) {
        System.out.println("Starte Löschen mit Heap mit " + heap.length + " Bäumen");
    }

    @Override
    public void logIntermediateStep(List<BinomialTreeNode> heap) {
        System.out.println("Zwischenschritt mit " + heap.size() + " Bäumen");
    }

    @Override
    public void logIntermediateStep(BinomialTreeNode[] heap) {
        System.out.println("Zwischenschritt mit " + heap.length + " Bäumen");
    }

    @Override
    public void addToIntermediateStep(List<BinomialTreeNode> additionalHeap) {
        System.out.println("Es wurden " + additionalHeap.size()
                + " weitere Bäume zum Zwischenschritt hinzugefügt.\nDas geht nur, wenn du schon einen Zwischenschritt dafür hinzugefügt hast!");
    }

    @Override
    public void addToIntermediateStep(BinomialTreeNode[] additionalHeap) {
        System.out.println("Es wurden " + additionalHeap.length
                + " weitere Bäume zum Zwischenschritt hinzugefügt.\nDas geht nur, wenn du schon einen Zwischenschritt dafür hinzugefügt hast!");
    }

    @Override
    public void addToIntermediateStep(BinomialTreeNode extraTree) {
        System.out.println(
                "Es wurde ein weiter Baum zum Zwischenschritt hinzugefügt.\nDas geht nur, wenn du schon einen Zwischenschritt dafür hinzugefügt hast!");

    }
}
