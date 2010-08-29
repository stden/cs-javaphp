package ru.ipo.dces.plugins;

import java.applet.*;
import java.io.File;
import java.net.*;
import java.util.Hashtable;
import javax.swing.JApplet;

public class MyAppletStub implements AppletStub{
    AppletContext ac;
    Hashtable<String, String> parameters=new Hashtable<String, String>();
    JApplet applet;
    
    boolean isActive=false;

    public MyAppletStub( JApplet applet,AppletContext ac, Hashtable<String, String> params)
    {
        this.applet=applet;
        this.ac=ac;
        this.parameters=params;
    }

    /*
    public void activate(){
        isActive=true;
    }

    public void disactiate(){
        isActive=false;
    }
    */

    public boolean isActive() {
        return isActive;
    }

    public URL getDocumentBase(){
        URL url;
        try {
            String db=(String)parameters.get("documentBase");
            File file = new File(db);
            url = file.toURI().toURL();
        } catch (MalformedURLException e){
            System.out.println("Bad URL");
            url = null;
        }
        return url;
    }

    public URL getCodeBase() {
        URL url = null;
        try {
            String db = parameters.get("documentBase");
            File file = new File(db);
            url = file.toURI().toURL();
        } catch (MalformedURLException e){
            System.out.println("Bad URL");
            url = null;
        }
        return url;
        
   }

    public String getParameter(String name) {
       return parameters.get(name);
    }

    public AppletContext getAppletContext() {
        return ac;
    }

    public void appletResize(int width, int height) {
      System.out.printf("%d x %d\n", width, height);
        //Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        //applet.setPreferredSize(new Dimension(660, 500));
    }

}
