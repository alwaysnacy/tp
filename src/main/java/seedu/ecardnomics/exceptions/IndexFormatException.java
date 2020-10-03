package seedu.ecardnomics.exceptions;

public class IndexFormatException extends Exception {
    public static final String INDEX_FORMAT_LINE =
            "Command should be followed by <index>";

    @Override
    public String getMessage() {
        return INDEX_FORMAT_LINE;
    }
}
