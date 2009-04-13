package ru.ipo.dces.client;

import ru.ipo.dces.pluginapi.Plugin;
import ru.ipo.dces.clientservercommunication.ContestDescription;

/**
 * Created by IntelliJ IDEA.
 * User: Посетитель
 * Date: 13.04.2009
 * Time: 16:07:02
 */
public interface AdminPlugin extends Plugin {

  void contestSelected(ContestDescription contest);

}
