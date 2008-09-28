package ru.ipo.dces.client;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import org.jdesktop.application.Application;

import ru.ipo.dces.clientservercommunication.*;
import ru.ipo.dces.pluginapi.Client;

public class ClientDialog extends JDialog implements Client {

  public class OpenPanelAction implements ActionListener {
    private final JPanel panel;

    public OpenPanelAction(JPanel panel) {
      this.panel = panel;
    }

    public void actionPerformed(ActionEvent evt) {
      System.out.println("userPanelButton.actionPerformed, event=" + evt);
      splitPane.add(panel, JSplitPane.RIGHT);
    }
  }

  private static final long serialVersionUID = -5765060013197859310L;

  /**
   * ������ ������� � �������� ������
   * 
   * @param args
   */
  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        JFrame frame = new JFrame();
        ClientDialog inst = new ClientDialog(frame);
        inst.setVisible(true);
      }
    });
  }

  private JSplitPane splitPane;
  private JPanel     rightPanel;
  private JPanel     leftPanel;
  private JButton    userPanelButton;
  public IServer     server;

  public ClientDialog(JFrame frame) {
    super(frame);
    initGUI();
  }

  private void initGUI() {
    try {
      BoxLayout thisLayout = new BoxLayout(getContentPane(),
          javax.swing.BoxLayout.X_AXIS);
      getContentPane().setLayout(thisLayout);
      {
        splitPane = new JSplitPane();
        getContentPane().add(splitPane);
        rightPanel = new AdminPanel();
        splitPane.add(rightPanel, JSplitPane.RIGHT);
        {

          leftPanel = new JPanel();
          GridLayout bLayout = new GridLayout(10, 1);
          bLayout.setColumns(1);
          bLayout.setHgap(2);
          bLayout.setVgap(2);
          leftPanel.setLayout(bLayout);
          splitPane.add(leftPanel, JSplitPane.LEFT);
          {
            userPanelButton = new JButton();
            userPanelButton.setText("User Panel");
            userPanelButton.setName("User Panel");
            userPanelButton.addActionListener(new OpenPanelAction(rightPanel));
            leftPanel.add(userPanelButton, BorderLayout.NORTH);

            for (int i = 2; i < 7; i++) {
              JButton panelButton = new JButton();
              panelButton.setText("Plugin " + i);
              panelButton.addActionListener(new OpenPanelAction(new JPanel()));
              leftPanel.add(panelButton, BorderLayout.NORTH);
            }
          }
        }
      }
      setSize(400, 300);
      setTitle("DCES Client");

      Application.getInstance().getContext().getResourceMap(getClass())
          .injectComponents(getContentPane());
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  @Override
  public SubmitSolutionResponse submitSolution(SubmitSolutionRequest solution) {
    // TODO Auto-generated method stub
    return null;
  }

}
