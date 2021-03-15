package pgdp.filetest;

//This exception is thrown when preparing/cleaning up the test environment fails
public class TestSetupFailedException extends RuntimeException {
    private static final long serialVersionUID = -2772131016686525204L;

    public TestSetupFailedException(String e) {
        super(e);
    }
}
