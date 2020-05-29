package pgdp.files;

import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

public class Result {

    private Path file;
    private List<Match> matches = new LinkedList<>();

    public Result(Path file) {
        this.file = file;
    }

    public void addMatch(Match match) {
        matches.add(match);
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    public Path getFile() {
        return file;
    }

    public void setFile(Path file) {
        this.file = file;
    }

    public String toString() {
        if (matches.isEmpty()) {
            return "Keine Übereinstummungen in der Datei " + file;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(matches.size());
        builder.append(" Übereinstimmung" + (matches.size() == 1 ? "" : "en"));
        builder.append(" in der Datei " + file + ".\n");
        for (int i = 0; i < matches.size(); i++) {
            builder.append(matches.get(i));
            if (i != matches.size() - 1) {
                builder.append("\n");
            }
        }
        return builder.toString();
    }


}
