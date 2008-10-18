package ru.ipo.dces.client;

import javax.swing.JOptionPane;

import ru.ipo.dces.clientservercommunication.*;
import ru.ipo.dces.pluginapi.*;

public class ClientImpl implements Client {

  private static ClientImpl instance = new ClientImpl();

  public static Client getInstance() {
    return instance;
  }

  private ClientImpl() {

  }

  // hashmap<Plugin, PluginInfo>

  // ����� �������, ��� ������� ������� ������������� ���� ������, ����� ������
  // ����������� �����, ��� ��� ������

  @Override
  public void setTitle(Plugin plugin, String title) {
    PluginInfo info = ClientData.plugin2info.get(plugin);
    if (info != null)
      info.getPluginButton().setText(title);
  }

  @Override
  public Response submitSolution(Request solution) {
    JOptionPane.showMessageDialog(null, solution.toString(), "���������",
        JOptionPane.INFORMATION_MESSAGE);
    return null;
  }

}
