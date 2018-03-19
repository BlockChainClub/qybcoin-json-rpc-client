package one.chainclub.qybcoin.jsonrpcclient;

public class QybcoinRPCException extends QybcoinException {
    /**
     * Creates a new instance of
     * <code>QybcoinRPCException</code> without detail message.
     */
    public QybcoinRPCException() {
    }

    /**
     * Constructs an instance of
     * <code>QybcoinRPCException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public QybcoinRPCException(String msg) {
        super(msg);
    }

    public QybcoinRPCException(Throwable cause) {
        super(cause);
    }

    public QybcoinRPCException(String message, Throwable cause) {
        super(message, cause);
    }
}
