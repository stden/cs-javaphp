package ru.ipo.dces.client;

import ru.ipo.dces.plugins.admin.*;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Посетитель
 * Date: 13.04.2009
 * Time: 16:28:20
 */
public class Localization {

  public final static String LOGGER_NAME = "Система";
  private final static HashMap<Class<? extends AdminPlugin>, String> adminPlugin2name;

  static {
    adminPlugin2name = new HashMap<Class<? extends AdminPlugin>, String>();
    adminPlugin2name.put(ResultsPlugin.class, "Результаты");
    adminPlugin2name.put(AdjustContestsPlugin.class, "Настроить контест");
    adminPlugin2name.put(CreateContestPlugin.class, "Создать контест");
    adminPlugin2name.put(LoginPluginV2.class, "Контесты");
    adminPlugin2name.put(LogoutPlugin.class, "Управление");
    adminPlugin2name.put(ManageUsersPlugin.class, "Пользователи");
    adminPlugin2name.put(PluginsManagementPlugin.class, "Плагины");
  }

  public static String getAdminPluginName(Class<? extends AdminPlugin> plugin) {
    return adminPlugin2name.get(plugin); 
  }
}
