package ru.ipo.dces.plugins;


import javax.swing.*;

import ru.ipo.dces.pluginapi.DCESPluginLoadable;
import ru.ipo.dces.pluginapi.Plugin;
import ru.ipo.dces.pluginapi.PluginEnvironment;
@DCESPluginLoadable
public class PluginInt implements Plugin{
	  MathkitAppletPanel mapanel;
	  static PluginEnvironment pe;
	  
	public PluginInt(PluginEnvironment pe){
    System.out.println("@#$ MathKit Plugin Created 4");
		PluginInt.pe=pe;
    mapanel = new MathkitAppletPanel();
    pe.setTitle(pe.getProblemName());
    System.out.println("pe.getProblemName() = " + pe.getProblemName());
  }
	
  public void activate() {
		// TODO Auto-generated method stub		
	}

	public void deactivate() {
		// TODO Auto-generated method stub		
	}

	public JPanel getPanel() {
		// TODO Auto-generated method stub
		return mapanel;
	}
	
}
