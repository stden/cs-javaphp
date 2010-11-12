package ru.ipo.dces.buildutils.raw;

/**
 * Описание задачи. Не является запросом или ответом, но используется в них
 */
public class ProblemDescription {

    /**
     * идентификатор задачи
     */
    @BinInfo(
            phpDefaultValue="-1",
            editable = false
    )
    public int id;

    /**
     * Binary data with ZipProblem
     */
    @BinInfo(
            phpDefaultValue="null"
    )
    public byte[] problem;

    @BinInfo(
            phpDefaultValue="",
            defaultValue = BinInfo.NEW_INSTANCE_DEFAULT_VALUE
    )
    public ContestSpecificSettings settings;

    /**
     * Идентификатор плагина стороны клиента, с помощью которого участник будет решать задачу
     *
     * @deprecated используется поле problem
     */
    @BinInfo(phpDefaultValue="'Test client plugin'", editable = false)
    public String clientPluginAlias;

    /**
     * Идентификатор плагина стороны сервера, который будет обрабатывать ответы участника
     *
     * @deprecated используется поле problem
     */
    @BinInfo(phpDefaultValue="'Test server plugin'", editable = false)
    public String serverPluginAlias;

    /**
     * Название задачи
     *
     * @deprecated используется поле problem
     */
    @BinInfo(phpDefaultValue="'Test problem'.rand(0,239239)", editable = false)
    public String name;

    /**
     * Условие задачи. Это конкретное условие задачи для некоторого участника. Поле заполняется сервером в его
     * ответах, в запросах клиента поле игнорируется
     *
     * @deprecated используется поле problem
     */
    @BinInfo(phpDefaultValue="null", editable = false)
    public byte[] statement;

    /**
     * Условие задачи. Это данные, которые используются для создания условия.
     *
     * @deprecated используется поле problem
     */
    @BinInfo(phpDefaultValue="null", editable = false)
    public byte[] statementData;

    /**
     * Ответ к задаче. Это данные, которые используются для создания ответа
     *
     * @deprecated используется поле problem
     */
    @BinInfo(phpDefaultValue="null", editable = false)
    public byte[] answerData;

}
