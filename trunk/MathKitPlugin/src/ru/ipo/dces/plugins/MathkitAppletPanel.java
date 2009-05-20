package ru.ipo.dces.plugins;


import java.awt.*;
import java.io.IOException;
import javax.swing.*;


public class MathkitAppletPanel extends JPanel {

  /**
   *
   */
  private static final long serialVersionUID = 1L;
  BorderLayout borderLayout1 = new BorderLayout();
  MathkitApplet mathkitapplet;

  public MathkitAppletPanel() {
    try {
      init();
      setApplet();


    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  private void init() {
    this.setLayout(borderLayout1);
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    this.setPreferredSize(new Dimension(660,500));
    ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);
    mathkitapplet = new MathkitApplet();
    this.add(mathkitapplet, BorderLayout.WEST);
  }

  public void setApplet() throws IOException {
    mathkitapplet.setPathApplet();
  } 
}


