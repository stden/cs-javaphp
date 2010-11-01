package ru.ipo.dces.debug;

import ru.ipo.dces.exceptions.GeneralRequestFailureException;

import java.util.HashMap;
import java.io.File;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Илья
 * Date: 12.12.2008
 * Time: 20:39:50
 *
 * Этот интерфейс соответствует плагину стороны сервера. Его нужно реализовать, если необходимо тестировать
 * плагин стороны клиента без подключения к реальному серверу.
 */
public interface ServerPluginEmulator {
  /**
   * <p>Метод вызывается для проверки решения участника. На вход он получает решение, а возвращает результат проверки.
   * Все решения участника и результат проверки имеют один и тот же
   * тип - это <tt>HashMap String String</tt>
   * <p>Помимо нового решения участника метод получает "состояние решения задач" иили null, если
   * метод вызывается первый раз. Состояние необходимо в том случае, если, например, надо запретить
   * пользователю посылать решение задачи больее чем определенное число раз. В этом случае состояние будет
   * содержать номер посылки решения, и если очередной раз этот номер стал слишком большим, проверку можно
   * больше не делать.
   * <p>Вообще-то реальный серверный плагин получает на вход еще и ответ к задаче, чтобы знать, с чем сравнивать
   * решение участника. Но здесь для простоты ответ предполагается, зашитым в реализацию метода checkSolution(). 
   * @param solution решение участника
   * @param result изначально пустой <tt>HashMap</tt>, который необходимо заполнить результатом проверки
   * @param state состояние задачи для данного участника. При первом вызове проверки решения равен null
   * @return новое состояние решения
   * @throws ru.ipo.dces.exceptions.GeneralRequestFailureException if communication with a server failed
   */
  public abstract Object checkSolution(HashMap<String, String> solution, HashMap<String, String> result, Object state)
          throws GeneralRequestFailureException;

  /**
   * <p>Метод возвращает каталог, где хранится условие задачи. Плагин стороны клиента должен сам знать,
   * как называются файлы в каталоге с нужной информацией.
   * <p>Метод вызывается ровно один раз при загрузке решения. Поэтому, если предполагается разным участникам
   * давать разные условия задач, метод может сначала создавать случайное условие, а потом возвращать каталог,
   * в котором оно лежит. 
   * @return каталог с условием задачи
   * @throws ru.ipo.dces.exceptions.GeneralRequestFailureException if communication with a server failed
   * @throws java.io.IOException if failed to write folder with statement
   */
  public abstract File getStatement() throws GeneralRequestFailureException, IOException;

}
