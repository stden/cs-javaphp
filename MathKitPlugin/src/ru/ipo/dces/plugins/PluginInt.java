package ru.ipo.dces.plugins;

import javax.swing.*;

import ru.ipo.dces.pluginapi.DCESPluginLoadable;
import ru.ipo.dces.pluginapi.Plugin;
import ru.ipo.dces.pluginapi.PluginEnvironment;

@DCESPluginLoadable
public class PluginInt implements Plugin{
	  private MathkitAppletPanel mapanel = null;
	  static PluginEnvironment pe;
	  
	public PluginInt(PluginEnvironment pe){
    System.out.println("@#$ MathKit Plugin Created 4");
		PluginInt.pe=pe;
    
    pe.setTitle(pe.getProblemName());
    System.out.println("pe.getProblemName() = " + pe.getProblemName());
  }
	
  public void activate() {
	}

	public void deactivate() {
	}

	public JPanel getPanel() {		
		if (mapanel == null)
      mapanel = new MathkitAppletPanel();
		return mapanel;
	}
	
}