package pgdp.files;

public class Match {

    private int lineIndex;
    private String line;

    public Match(int lineIndex, String line) {
        this.lineIndex = lineIndex;
        this.line = line;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public int getLineIndex() {
        return lineIndex;
    }

    public void setLineIndex(int lineIndex) {
        this.lineIndex = lineIndex;
    }

    public String toString() {
        return "Übereinstimmung in Zeile " + lineIndex + ": " + line.trim();
    }

}
