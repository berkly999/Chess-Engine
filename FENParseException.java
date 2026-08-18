public class FENParseException extends Exception {
    public FENParseException(String errorMessage) {
        super("Error Parsing FEN: " + errorMessage);
    }

    public FENParseException() {
        super("Error Parsing FEN");
    }
}