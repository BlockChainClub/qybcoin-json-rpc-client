package one.chainclub.qybcoin.jsonrpcclient;


public class QybcoinException extends Exception {

    /**
     * Creates a new instance of
     * <code>QybcoinException</code> without detail message.
     */
    public QybcoinException() {
    }

    /**
     * Constructs an instance of
     * <code>QybcoinException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public QybcoinException(String msg) {
        super(msg);
    }

    public QybcoinException(Throwable cause) {
        super(cause);
    }

    public QybcoinException(String message, Throwable cause) {
        super(message, cause);
    }
}
