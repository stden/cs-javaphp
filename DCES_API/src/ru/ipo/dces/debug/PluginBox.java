package ru.ipo.dces.debug;

import ru.ipo.dces.exceptions.GeneralRequestFailureException;
import ru.ipo.dces.exceptions.ServerReturnedError;
import ru.ipo.dces.pluginapi.Plugin;
import ru.ipo.dces.pluginapi.PluginEnvironment;
import ru.ipo.dces.log.LogMessageType;
import ru.ipo.dces.log.LoggerFactory;
import ru.ipo.dces.log.UserMessagesLogger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: ����
 * Date: 12.12.2008
 * Time: 21:40:50
 *
 * ����� ��������� ���� ��� ������������ �������
 */
public class PluginBox extends JFrame {  

  private JSplitPane        splitPane;
  private JPanel            leftPanel        = null;
  private ButtonGroup       pluginsButtonGroup = new ButtonGroup();
  private JToggleButton     pluginButton;
  private JToggleButton     otherPluginButton;
  private Plugin            plugin;
  private Plugin            otherPlugin;
  private ServerPluginEmulator serverEmulator;
  private UserMessagesLogger logger = LoggerFactory.getLogger();

  /**
   * ������� ���� ��� ������������ ������� ������� �������. ������
   * ������������ ������� � ���� ���������� ������ ������, �� ������� ����� �������������.
   * ��� ����� ��� ������������ ����, ��� ������ ������������ ������� <tt>activate()</tt> �
   * <tt>deactivate()</tt>.
   * <p>
   * ��� ������������ ������� � ���� ������ ���� ��������� ������. ������� ������ ����������� � �������
   * ������� ������� serverEmulator. ��� ������ ���������� � �������� ���������� ���������. ���� ���
   * ������ ��������� �� �������, ����� <tt>PluginBox</tt> ����� ������������ �����������
   * @see PluginBox#PluginBox(Class, ServerPluginEmulator)   
   * @param pluginClass �����, �������������� ������������ �������
   * @param serverEmulator ���������� �������� ������� ������� �������
   * @param problemName ��� ������
   */
  public PluginBox(Class<? extends Plugin> pluginClass, ServerPluginEmulator serverEmulator, String problemName) {
    super();

    initGUI();

    otherPlugin = new OtherPlugin(null);

    this.serverEmulator = serverEmulator;
    
    final PluginEnvironmentImpl pluginEnvironment;
    try {
      pluginEnvironment = new PluginEnvironmentImpl();

      pluginEnvironment.problemName = problemName;
      final Constructor<? extends Plugin> PluginConstructor = pluginClass.getConstructor(PluginEnvironment.class);
      plugin = PluginConstructor.newInstance(pluginEnvironment);
    } catch (InvocationTargetException e) {
      LoggerFactory.getLogger().log(
              "plugin throws exception in constrctor (see stack trace)",
              LogMessageType.Error,
              null
      );
      e.getCause().printStackTrace();
      setErrorPlugin();
    } catch (Exception e) {
      LoggerFactory.getLogger().log(
              "exception while initializing plugin (see stack trace)",
              LogMessageType.Error,
              null
      );
      e.printStackTrace();
      setErrorPlugin();
    }

    setRightPanel(otherPlugin.getPanel());
  }

  private void setErrorPlugin() {
    plugin = new OtherPlugin(null);
    pluginButton.setText("ERROR !");
    ((OtherPlugin)plugin).setLabel("�� ������� ��������� ����� � ��������");
  }

  /**
   * ����������� ���������� ������� ������������, ������ ����� �� ����������� ��� ������, ��� ����������
   * �� ��������� � ���������� ������ "Unnamed problem" 
   * @see 	PluginBox#PluginBox(Class, ServerPluginEmulator, String)
   * @param pluginClass ����� �������, ������� ���������� �����������
   * @param serverEmulator ���������� �������� ���������� �������
   */
  public PluginBox(Class<? extends Plugin> pluginClass, ServerPluginEmulator serverEmulator){
    this(pluginClass, serverEmulator, "Unnamed problem");
  }

  private void initGUI() {
    BoxLayout thisLayout = new BoxLayout(getContentPane(), javax.swing.BoxLayout.X_AXIS);
    getContentPane().setLayout(thisLayout);

    splitPane = new JSplitPane();
    getContentPane().add(splitPane);
    leftPanel = new JPanel();
    leftPanel.setName("Left panel");
    GridLayout bLayout = new GridLayout(10, 1);
    bLayout.setColumns(1);
    bLayout.setHgap(2);
    bLayout.setVgap(2);
    leftPanel.setLayout(bLayout);
    splitPane.add(leftPanel, JSplitPane.LEFT);

    initialState();

    setSize(800, 600);
    // ���������� ���� �� ������ ������
    setLocationRelativeTo(null);
    setTitle("DCES client plugin test suite");
  }

  /** ��������� ��������� ������� �� ������������� �������� */
  private void initialState() {
    otherPluginButton = new JToggleButton();
    pluginButton = new JToggleButton();
    pluginsButtonGroup.add(pluginButton);
    pluginsButtonGroup.add(otherPluginButton);
    otherPluginButton.setSelected(true);

    otherPluginButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        plugin.deactivate();
        setRightPanel(otherPlugin.getPanel());
      }
    });

    pluginButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        plugin.activate();
        setRightPanel(plugin.getPanel());
      }
    });

    leftPanel.add(otherPluginButton);
    leftPanel.add(pluginButton);
  }

  private void setRightPanel(JPanel panel) {
    splitPane.add(panel, JSplitPane.RIGHT);
  }

  private class OtherPlugin extends JPanel implements Plugin {

    private final JLabel jLabel; 

    /**
     * @param env environment for the plugin
     */
    @SuppressWarnings({"UnusedDeclaration", "UnusedDeclaration", "UnusedDeclaration"})
    public OtherPlugin(PluginEnvironment env) {
      this.setLayout(new GridLayout(1,1));
      jLabel = new JLabel("��� ��������� ������ ������� ������� ������ �����");
      jLabel.setHorizontalAlignment(JLabel.CENTER);
      this.add(jLabel);

      otherPluginButton.setText("Other Plugin");
    }

    public void setLabel(String s) {
      jLabel.setText(s);
    }

    public JPanel getPanel() {
      return this;
    }

    public void activate() {
      //do nothing
    }

    public void deactivate() {
      //do nothing
    }
  }

  private class PluginEnvironmentImpl implements PluginEnvironment {

    private Object problemState = null;
    public String problemName;
    private final File problemFolder;

    private PluginEnvironmentImpl() throws IOException, GeneralRequestFailureException, ServerReturnedError {
      problemFolder = serverEmulator.getStatement();
    }

    public void setTitle(String title) {
      pluginButton.setText(title);
    }

    public HashMap<String, String> submitSolution(HashMap<String, String> solution) throws GeneralRequestFailureException {
      HashMap<String, String> result = new HashMap<String, String>();
      problemState = serverEmulator.checkSolution(solution, result, problemState);      
      return result;
    }

    public File getProblemFolder() {
      return problemFolder;
    }

    public String getProblemName() {
      return problemName;
    }

    public void log(String message, LogMessageType type) {
      logger.log(message, type, problemName);
    }
  }

}
