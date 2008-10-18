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

  // можно сделать, что каждому плагину соответствует свой клиент, тогда клиент
  // обязательно знает, кто его вызвал

  @Override
  public void setTitle(Plugin plugin, String title) {
    PluginInfo info = ClientData.plugin2info.get(plugin);
    if (info != null)
      info.getPluginButton().setText(title);
  }

  @Override
  public Response submitSolution(Request solution) {
    JOptionPane.showMessageDialog(null, solution.toString(), "Сообщение",
        JOptionPane.INFORMATION_MESSAGE);
    return null;
  }

}
