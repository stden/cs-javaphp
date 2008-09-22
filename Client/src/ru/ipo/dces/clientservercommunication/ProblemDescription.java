package ru.ipo.dces.clientservercommunication;

/**
 * Описание задачи
 */
public class ProblemDescription {
  /** идентификатор задачи */
  public String id;
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

}
