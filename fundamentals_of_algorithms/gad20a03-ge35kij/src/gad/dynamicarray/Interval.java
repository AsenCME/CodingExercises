package gad.dynamicarray;

/**
 * Objekte der Klasse Interval stellen einen Bereich von Zahlen dar. Die
 * Intervallgrenzen sind dabei jeweils eingeschlossen.
 */
abstract class Interval {
    /**
     * Abfragen der unteren Intervallgrenze
     * 
     * @return die untere Grenze
     */
    public abstract int getFrom();

    /**
     * Abfragen der oberen Intervallgrenze
     * 
     * @return die obere Grenze
     */
    public abstract int getTo();

    /**
     * Diese Methode gibt die Größe des Intervalls zurück. Zu
     * beachten ist dabei, dass falls getFrom() > getTo() gilt,
     * das Intervall die Elemente in den Teilintervallen
     * [usage.getFrom(); baseSize - 1] und [0; usage.getTo()] abdeckt.
     * 
     * @param baseSize die Basisgröße, anhand derer ein Umbruch
     *                 innerhalb des Intervalls festgestellt wird
     * @return die Menge an abgedeckten Elementen
     */
    public abstract int getSize(int baseSize);

    /**
     * Gibt zurück, ob das Intervall leer ist.
     * 
     * @return 'true' falls das Intervall leer ist, 'false' sonst
     */
    public abstract boolean isEmpty();

    public abstract String toString();

    public abstract boolean equals(Object obj);
}

/**
 * Objekte der Klasse NonEmptyInterval repräsentieren ein nicht-leeres
 * Intervall.
 */
class NonEmptyInterval extends Interval {
    private int from;

    @Override
    public int getFrom() {
        return from;
    }

    private int to;

    @Override
    public int getTo() {
        return to;
    }

    public NonEmptyInterval(int from, int to) {
        if (from < 0 || to < 0)
            throw new RuntimeException("Negative interval boundaries are invalid!");
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "[" + from + ";" + to + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof NonEmptyInterval) {
            NonEmptyInterval other = (NonEmptyInterval) obj;
            return from == other.from && to == other.to;
        } else {
            return false;
        }
    }

    @Override
    public int getSize(int baseSize) {
        if (to >= baseSize || from >= baseSize)
            throw new RuntimeException("Invalid interval for this base size!");
        if (to >= from)
            return to - from + 1;
        else
            return baseSize - (from - to - 1);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}

/**
 * Objekte der Klasse EmptyInterval repräsentieren ein leeres Intervall.
 */
class EmptyInterval extends Interval {
    @Override
    public int getFrom() {
        throw new RuntimeException("No lower boundary in empty interval");
    }

    @Override
    public int getTo() {
        throw new RuntimeException("No upper boundary in empty interval");
    }

    public EmptyInterval() {
    }

    @Override
    public String toString() {
        return "[]";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof EmptyInterval) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int getSize(int baseSize) {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }
}