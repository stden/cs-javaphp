import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JFrame;

import ru.ipo.dces.debug.PluginBox;
import ru.ipo.dces.debug.ServerPluginEmulator;
import ru.ipo.dces.exceptions.GeneralRequestFailureException;
import ru.ipo.dces.plugins.PluginInt;


public class TestMathKitPlugin2 {

	/**
	 * @param args
	 */
	 public static void main(String[] args) {

		    ServerPluginEmulator spe = new ServerPluginEmulator() {
		      public Object checkSolution(HashMap<String, String> solution, HashMap<String, String> result, Object state) throws GeneralRequestFailureException {
		        System.out.println("SOLUTION = " + solution.toString());
		        return null;
		      }

		      public File getStatement() throws GeneralRequestFailureException, IOException {
		        return new File("debug/c11");
		      }
		    };

		    PluginBox pb = new PluginBox(PluginInt.class, spe);
		    pb.setVisible(true);
		    pb.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		  }


}
