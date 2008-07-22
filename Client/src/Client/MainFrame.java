package Client;

import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import java.awt.*;

import javax.swing.JFrame;

public class MainFrame extends JFrame {

  private static final long serialVersionUID = 1L;
  private JPanel            jContentPane     = null;

  public MainFrame() throws HeadlessException {
    // TODO Auto-generated constructor stub

    super();
    initialize();
  }

  public MainFrame(GraphicsConfiguration arg0) {
    super(arg0);
    // TODO Auto-generated constructor stub

    initialize();
  }

  public MainFrame(String arg0) throws HeadlessException {
    super(arg0);
    // TODO Auto-generated constructor stub

    initialize();
  }

  public MainFrame(String arg0, GraphicsConfiguration arg1) {
    super(arg0, arg1);
    // TODO Auto-generated constructor stub

    initialize();
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    // TODO Auto-generated method stub

    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        MainFrame thisClass = new MainFrame();
        thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        thisClass.setVisible(true);
      }
    });
  }

  /**
   * This method initializes this
   * 
   * @return void
   */
  private void initialize() {
    this.setSize(300, 200);
    this.setContentPane(getJContentPane());
    this.setTitle("JFrame");
  }

  /**
   * This method initializes jContentPane
   * 
   * @return javax.swing.JPanel
   */
  private JPanel getJContentPane() {
    if (jContentPane == null) {
      jContentPane = new JPanel();
      jContentPane.setLayout(new BorderLayout());
    }
    return jContentPane;
  }

}
