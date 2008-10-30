package ru.ipo.dces.clientservercommunication;

/**
 * Created by IntelliJ IDEA.
 * User: ����
 * Date: 30.10.2008
 * Time: 1:28:06
 */
public class ServerReturnedNoAnswer extends Exception {

    public ServerReturnedNoAnswer() {
    }

    public ServerReturnedNoAnswer(String message) {
        super(message);
    }

    public ServerReturnedNoAnswer(String message, Throwable cause) {
        super(message, cause);
    }

    public ServerReturnedNoAnswer(Throwable cause) {
        super(cause);
    }
}
