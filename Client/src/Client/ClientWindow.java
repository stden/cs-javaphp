package Client;

import javax.swing.JDesktopPane;

public class ClientWindow extends JDesktopPane {

  private static final long serialVersionUID = 1L;

  /**
   * This is the default constructor
   */
  public ClientWindow() {
    super();
    initialize();
  }

  /**
   * This method initializes this
   * 
   * @return void
   */
  private void initialize() {
    this.setSize(300, 200);

  }

}
