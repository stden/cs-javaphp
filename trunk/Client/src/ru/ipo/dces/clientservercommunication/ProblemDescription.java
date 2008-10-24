package ru.ipo.dces.clientservercommunication;

/**
 * Описание задачи
 */
public class ProblemDescription implements Comparable<ProblemDescription> {

  /** идентификатор задачи */
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
  public byte[] problemData;

  @Override
  public int compareTo(ProblemDescription o) {
    return name.compareTo(o.name);
  }

}
