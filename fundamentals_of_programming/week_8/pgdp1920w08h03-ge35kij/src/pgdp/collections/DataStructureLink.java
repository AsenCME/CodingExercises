package pgdp.collections;

public class DataStructureLink<T> {
    DataStructureConnector<T> a, b;

    public DataStructureLink(DataStructureConnector<T> _a, DataStructureConnector<T> _b) {
        a = _a;
        b = _b;
    }

    public boolean moveNextFromAToB() {
        if (!a.hasNextElement())
            return false;

        T temp = a.removeNextElement();
        b.addElement(temp);
        return true;
    }

    public void moveAllFromAToB() {
        if (!a.hasNextElement())
            return;

        while (a.hasNextElement())
            b.addElement(a.removeNextElement());
    }
}