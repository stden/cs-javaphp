package ru.ipo.dces.client;

import ru.ipo.dces.plugins.admin.*;
import ru.ipo.dces.pluginapi.Plugin;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: ����������
 * Date: 13.04.2009
 * Time: 16:28:20
 */
public class Localization {

  public final static String LOGGER_NAME = "�������";
  private final static HashMap<Class<? extends Plugin>, String> adminPlugin2name;

  static {
    adminPlugin2name = new HashMap<Class<? extends Plugin>, String>();
    adminPlugin2name.put(ResultsPlugin.class, "����������");
//    adminPlugin2name.put(AdjustContestsPlugin.class, "��������� �������");
    adminPlugin2name.put(ContestPluginV2.class, "��������� �������");
//    adminPlugin2name.put(CreateContestPlugin.class, "������� �������");
    adminPlugin2name.put(LoginPlugin.class, "��������");
    adminPlugin2name.put(LogoutPlugin.class, "����������");
    adminPlugin2name.put(ManageUsersPlugin.class, "������������");
    adminPlugin2name.put(PluginsManagementPlugin.class, "�������");
  }

  public static String getAdminPluginName(Class<? extends Plugin> plugin) {
    return adminPlugin2name.get(plugin); 
  }
}
