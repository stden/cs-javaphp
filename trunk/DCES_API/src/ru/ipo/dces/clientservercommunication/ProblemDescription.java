package ru.ipo.dces.clientservercommunication;

/**
 * Описание задачи. Не является запросом или ответом, но используется в них
 */
public class ProblemDescription {

    /**
     * идентификатор задачи
     */
    @PHPDefaultValue("-1")
    public int id;

    /**
     * Binary data with ZipProblem
     */
    @PHPDefaultValue("null")
    public byte[] problem;

    @PHPDefaultValue("")
    public ContestSpecificSettings settings;

    /**
     * Идентификатор плагина стороны клиента, с помощью которого участник будет решать задачу
     *
     * @deprecated используется поле problem
     */
    @PHPDefaultValue("'Test client plugin'")
    public String clientPluginAlias;

    /**
     * Идентификатор плагина стороны сервера, который будет обрабатывать ответы участника
     *
     * @deprecated используется поле problem
     */
    @PHPDefaultValue("'Test server plugin'")
    public String serverPluginAlias;

    /**
     * Название задачи
     *
     * @deprecated используется поле problem
     */
    @PHPDefaultValue("'Test problem'.rand(0,239239)")
    public String name;

    /**
     * Условие задачи. Это конкретное условие задачи для некоторого участника. Поле заполняется сервером в его
     * ответах, в запросах клиента поле игнорируется
     *
     * @deprecated используется поле problem
     */
    @PHPDefaultValue("null")
    public byte[] statement;

    /**
     * Условие задачи. Это данные, которые используются для создания условия.
     *
     * @deprecated используется поле problem
     */
    @PHPDefaultValue("null")
    public byte[] statementData;

    /**
     * Ответ к задаче. Это данные, которые используются для создания ответа
     *
     * @deprecated используется поле problem
     */
    @PHPDefaultValue("null")
    public byte[] answerData;

}
