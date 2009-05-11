package ru.ipo.dces.plugins;


import javax.swing.JPanel;

import ru.ipo.dces.pluginapi.DCESPluginLoadable;
import ru.ipo.dces.pluginapi.Plugin;
import ru.ipo.dces.pluginapi.PluginEnvironment;
@DCESPluginLoadable
public class PluginInt implements Plugin{
	  MathkitAppletPanel mapanel;
	  static PluginEnvironment pe;
	  
	public PluginInt(PluginEnvironment pe){
		PluginInt.pe=pe;
    mapanel = new MathkitAppletPanel();
    pe.setTitle(pe.getProblemName());
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
