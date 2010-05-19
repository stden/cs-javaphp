package ru.ipo.dces.plugins;


import java.awt.*;
import java.io.IOException;
import javax.swing.*;


public class MathkitAppletPanel extends JPanel {
  
  private MathkitApplet mathkitapplet;

  public MathkitAppletPanel() {
    try {
      init();
      setApplet();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void init() {
    this.setLayout(new BorderLayout());
    //this.setPreferredSize(new Dimension(660,500));
    ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);
    mathkitapplet = new MathkitApplet();    
    this.add(mathkitapplet, BorderLayout.CENTER);    
  }

  public void setApplet() throws IOException {
    mathkitapplet.setPathApplet();
  } 
}


