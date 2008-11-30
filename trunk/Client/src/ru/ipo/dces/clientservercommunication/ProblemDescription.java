package ru.ipo.dces.clientservercommunication;

/**
 * Описание задачи
 */
public class ProblemDescription implements Comparable<ProblemDescription> {

  /** идентификатор задачи, номер задачи в контесте*/
  public int    id;

  /** ID плагина, который будет обрабатывать задачу */
  public String clientPluginID;

  /** ID плагина, который стороны сервера, получает результаты по задаче */
  public String serverPluginID;

  /** название */
  public String name;

  /**
   * Условие задачи. Вероятнее всего, это архив, который будет автоматически
   * раскрываться в каталог, соответствующий задаче
   */
  public byte[] statement;

  /**
   * Условие задачи. Это данные, которые используются для создания условия
   */
  public byte[] statementData;

  /**
   * Ответ к задаче. Это данные, которые используются для создания ответа
   */
  public byte[] answerData;

  @Override
  public int compareTo(ProblemDescription o) {
    return name.compareTo(o.name);
  }

}
