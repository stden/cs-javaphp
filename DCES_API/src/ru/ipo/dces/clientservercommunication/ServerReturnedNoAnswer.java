package ru.ipo.dces.clientservercommunication;

/**
 * Created by IntelliJ IDEA.
 * User: Илья
 * Date: 30.10.2008
 * Time: 1:28:06
 */
public class ServerReturnedNoAnswer extends Exception {

    private String actualAnswer = null;

    public ServerReturnedNoAnswer() {
    }

    public ServerReturnedNoAnswer(String message) {
      super(message);
      //TODO make somehow the next line to come out only if defined DEBUG
      System.out.println("actual server answer unknown");
    }

    public ServerReturnedNoAnswer(String message, Throwable cause) {
        super(message, cause);
    }

    public ServerReturnedNoAnswer(Throwable cause) {
        super(cause);
    }

    public ServerReturnedNoAnswer(String message, String actualAnswer) {
      super(message);
      this.actualAnswer = actualAnswer;
      //TODO make somehow the next line to come out only if defined DEBUG
      System.out.println("actual server answer = " + actualAnswer + "\n");
    }

    public String getActualAnswer() {
      return actualAnswer;
    }
}
