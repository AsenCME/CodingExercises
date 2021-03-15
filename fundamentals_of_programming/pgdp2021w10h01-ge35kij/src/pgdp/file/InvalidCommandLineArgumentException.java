package pgdp.file;

public class InvalidCommandLineArgumentException extends Exception {
    private static final long serialVersionUID = -2261947453741894168L;

    public InvalidCommandLineArgumentException() {
        super();
    }

    public InvalidCommandLineArgumentException(String m) {
        super(m);
    }
}
