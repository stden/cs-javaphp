package ru.ipo.dces.server.http;

/** Неверный класс при десериализации */
class IllegalClassException extends Exception {

  private static final long serialVersionUID = 6239273096888694801L;

  public String             expected, actual;

  public IllegalClassException(String expected, String actual) {
    this.expected = expected;
    this.actual = actual;
  }

}
