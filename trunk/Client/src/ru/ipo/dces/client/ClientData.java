package ru.ipo.dces.client;

import java.util.HashMap;

import ru.ipo.dces.pluginapi.Plugin;

/** ������ � ���������� � �������� */
public class ClientData {
  public static ServerFacade                server;
  public static String                      sessionID;
  public static HashMap<Plugin, PluginInfo> plugin2info = new HashMap<Plugin, PluginInfo>();
}
