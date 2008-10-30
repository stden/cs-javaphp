package ru.ipo.dces.clientservercommunication;

/**
 * Created by IntelliJ IDEA.
 * User: Илья
 * Date: 30.10.2008
 * Time: 1:30:38
 */
public class ServerReturnedError extends Exception {

    public ServerReturnedError() {
    }

    public ServerReturnedError(String message) {
        super(message);
    }

    public ServerReturnedError(String message, Throwable cause) {
        super(message, cause);
    }

    public ServerReturnedError(Throwable cause) {
        super(cause);
    }
}
