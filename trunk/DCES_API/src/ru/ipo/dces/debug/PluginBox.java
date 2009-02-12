package ru.ipo.dces.debug;

import ru.ipo.dces.exceptions.GeneralRequestFailureException;
import ru.ipo.dces.pluginapi.Plugin;
import ru.ipo.dces.pluginapi.PluginEnvironment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: »ль€
 * Date: 12.12.2008
 * Time: 21:40:50
 *
 *  ласс реализует окно дл€ тестировани€ плагина
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

  /**
   * —оздает окно дл€ тестировани€ плагина стороны клиента. ѕомимо
   * тестируемого плагина в окне по€вл€етс€ другой плагин, на который можно переключатьс€.
   * Ёто нужно дл€ тестировани€ того, как плагин обрабатывает событи€ <tt>activate()</tt> и
   * <tt>deactivate()</tt>.
   * <p>
   * ƒл€ тестировани€ плагина в него должна быть загружена задача. ”словие задачи загружаетс€ с помощью
   * методов объекта serverEmulator. »м€ задачи передаетс€ в качестве отдельного параметра. ≈сли им€
   * задачи указывать не хочетс€, класс <tt>PluginBox</tt> имеет параллельный конструктор
   * @see PluginBox#PluginBox(Class, ServerPluginEmulator)   
   * @param pluginClass класс, соответсвующий тестируемому плагину
   * @param serverEmulator реализаци€ заглушки плагина стороны сервера
   * @param problemName им€ задачи
   */
  public PluginBox(Class<? extends Plugin> pluginClass, ServerPluginEmulator serverEmulator, String problemName) {
    super();

    initGUI();

    otherPlugin = new OtherPlugin(null);

    this.serverEmulator = serverEmulator;
    final PluginEnvironmentImpl pluginEnvironment = new PluginEnvironmentImpl();
    pluginEnvironment.problemName = problemName;
    try {
      final Constructor<? extends Plugin> PluginConstructor = pluginClass.getConstructor(PluginEnvironment.class);
      plugin = PluginConstructor.newInstance(pluginEnvironment);
    } catch (InvocationTargetException e) {
      System.err.println("plugin throws exception in constructor:");
      e.getCause().printStackTrace();
      setErrorPlugin();
    } catch (Exception e) {
      setErrorPlugin();
    }

    setRightPanel(otherPlugin.getPanel());
  }

  private void setErrorPlugin() {
    plugin = new OtherPlugin(null);
    pluginButton.setText("ERROR !");
    ((OtherPlugin)plugin).setLabel("Ќе удалось загрузить класс с плагином");
  }

  /**
   *  онструктор аналогичен другому контсруктору, только здесь не указываетс€ им€ задачи, оно выбираетс€
   * по умолчанию и становитс€ равным "Unnamed problem" 
   * @see 	PluginBox#PluginBox(Class, ServerPluginEmulator, String)
   * @param pluginClass класс плагина, который необходимо тестировать
   * @param serverEmulator реализаци€ заглушки серверного плагина
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
    // –азместить окно по центру экрана
    setLocationRelativeTo(null);
    setTitle("DCES client plugin test suite");
  }

  /** Ќачальное состо€ние клиента до присоединени€ контеста */
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
      jLabel = new JLabel("ƒл€ просмотра вашего плагина нажмите кнопку слева");
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

    private HashMap<String, String> previousResult = null;
    public String problemName;
    private final File problemFolder;

    private PluginEnvironmentImpl() {
      problemFolder = serverEmulator.getStatement();
    }

    public void setTitle(String title) {
      pluginButton.setText(title);
    }

    public HashMap<String, String> submitSolution(HashMap<String, String> solution) throws GeneralRequestFailureException {
      final HashMap<String, String> serverAnswer = serverEmulator.checkSolution(solution, previousResult);
      previousResult = new HashMap<String, String>(serverAnswer);
      return new HashMap<String, String>(serverAnswer);
    }

    public File getProblemFolder() {
      return problemFolder;
    }

    public String getProblemName() {
      return problemName;
    }
  }

}
